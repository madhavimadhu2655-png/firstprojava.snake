package com.snakegame;

import java.util.*;

public class Snake {
    private final String id;
    private final String color;
    private final String name;
    private final LinkedList<int[]> body;
    private String direction;
    private boolean alive;
    private int score;

    private static final int GRID_W = 40;
    private static final int GRID_H = 30;

    public Snake(String id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.direction = "RIGHT";
        this.alive = true;
        this.score = 0;
        this.body = new LinkedList<>();
        int startX = (int)(Math.random() * 30) + 5;
        int startY = (int)(Math.random() * 24) + 3;
        body.add(new int[]{startX, startY});
        body.add(new int[]{startX - 1, startY});
        body.add(new int[]{startX - 2, startY});
    }

    public void setDirection(String dir) {
        // Prevent reversing
        if (dir.equals("UP") && direction.equals("DOWN")) return;
        if (dir.equals("DOWN") && direction.equals("UP")) return;
        if (dir.equals("LEFT") && direction.equals("RIGHT")) return;
        if (dir.equals("RIGHT") && direction.equals("LEFT")) return;
        this.direction = dir;
    }

    /** Move the snake one step. Returns true if food was eaten (caller should grow). */
    public boolean move(int[] food) {
        if (!alive) return false;
        int[] head = body.getFirst();
        int nx = head[0], ny = head[1];
        switch (direction) {
            case "UP"    -> ny--;
            case "DOWN"  -> ny++;
            case "LEFT"  -> nx--;
            case "RIGHT" -> nx++;
        }
        // Wall collision
        if (nx < 0 || nx >= GRID_W || ny < 0 || ny >= GRID_H) {
            alive = false;
            return false;
        }
        // Self collision
        for (int[] seg : body) {
            if (seg[0] == nx && seg[1] == ny) {
                alive = false;
                return false;
            }
        }
        body.addFirst(new int[]{nx, ny});
        boolean atFood = (nx == food[0] && ny == food[1]);
        if (!atFood) {
            body.removeLast();
        } else {
            score++;
        }
        return atFood;
    }

    public boolean collidesWith(Snake other) {
        if (other == this) return false;
        int[] head = body.getFirst();
        for (int[] seg : other.body) {
            if (seg[0] == head[0] && seg[1] == head[1]) return true;
        }
        return false;
    }

    public int[] getHead() { return body.getFirst(); }
    public LinkedList<int[]> getBody() { return body; }
    public String getId() { return id; }
    public String getColor() { return color; }
    public String getName() { return name; }
    public String getDirection() { return direction; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public int getScore() { return score; }

    public void respawn() {
        alive = true;
        score = 0;
        direction = "RIGHT";
        body.clear();
        int startX = (int)(Math.random() * 30) + 5;
        int startY = (int)(Math.random() * 24) + 3;
        body.add(new int[]{startX, startY});
        body.add(new int[]{startX - 1, startY});
        body.add(new int[]{startX - 2, startY});
    }
}
