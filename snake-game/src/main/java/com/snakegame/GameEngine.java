package com.snakegame;

import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
public class GameEngine {

    private final Map<String, Snake> snakes = new ConcurrentHashMap<>();
    private int[] food;
    private final int GRID_W = 40;
    private final int GRID_H = 30;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<GameStateListener> listeners = new CopyOnWriteArrayList<>();

    public interface GameStateListener {
        void onStateUpdate(String json);
    }

    public GameEngine() {
        spawnFood();
        // Game tick: 150ms
        scheduler.scheduleAtFixedRate(this::tick, 500, 150, TimeUnit.MILLISECONDS);
    }

    private void tick() {
        try {
            for (Snake snake : snakes.values()) {
                if (!snake.isAlive()) continue;
                boolean ate = snake.move(food);
                if (ate) spawnFood();
            }
            // Check inter-snake collisions
            List<Snake> snakeList = new ArrayList<>(snakes.values());
            for (Snake s : snakeList) {
                if (!s.isAlive()) continue;
                for (Snake other : snakeList) {
                    if (s != other && s.collidesWith(other)) {
                        s.setAlive(false);
                    }
                }
            }
            broadcast();
        } catch (Exception e) {
            // ignore tick errors
        }
    }

    private void spawnFood() {
        Random r = new Random();
        food = new int[]{r.nextInt(GRID_W), r.nextInt(GRID_H)};
    }

    public void addPlayer(String id, String name) {
        String[] colors = {"#00ff88","#ff4466","#4488ff","#ffcc00","#ff88ff","#ff8800","#00ccff","#cc00ff"};
        String color = colors[snakes.size() % colors.length];
        snakes.put(id, new Snake(id, color, name));
    }

    public void removePlayer(String id) {
        snakes.remove(id);
    }

    public void setDirection(String id, String dir) {
        Snake s = snakes.get(id);
        if (s != null) s.setDirection(dir);
    }

    public void respawn(String id) {
        Snake s = snakes.get(id);
        if (s != null) s.respawn();
    }

    public void addListener(GameStateListener l) { listeners.add(l); }
    public void removeListener(GameStateListener l) { listeners.remove(l); }

    private void broadcast() {
        String json = buildStateJson();
        for (GameStateListener l : listeners) {
            l.onStateUpdate(json);
        }
    }

    public String buildStateJson() {
        JsonObject state = new JsonObject();

        JsonArray playersArr = new JsonArray();
        for (Snake s : snakes.values()) {
            JsonObject p = new JsonObject();
            p.addProperty("id", s.getId());
            p.addProperty("color", s.getColor());
            p.addProperty("name", s.getName());
            p.addProperty("alive", s.isAlive());
            p.addProperty("score", s.getScore());

            JsonArray bodyArr = new JsonArray();
            for (int[] seg : s.getBody()) {
                JsonObject cell = new JsonObject();
                cell.addProperty("x", seg[0]);
                cell.addProperty("y", seg[1]);
                bodyArr.add(cell);
            }
            p.add("body", bodyArr);
            playersArr.add(p);
        }
        state.add("players", playersArr);

        JsonObject foodObj = new JsonObject();
        foodObj.addProperty("x", food[0]);
        foodObj.addProperty("y", food[1]);
        state.add("food", foodObj);

        return new Gson().toJson(state);
    }
}
