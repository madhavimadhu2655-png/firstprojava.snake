package com.snakegame;

import com.google.gson.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameHandler extends TextWebSocketHandler {

    private final GameEngine engine;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public GameHandler(GameEngine engine) {
        this.engine = engine;
        engine.addListener(json -> {
            for (WebSocketSession s : sessions.values()) {
                try {
                    if (s.isOpen()) s.sendMessage(new TextMessage(json));
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        sessions.put(id, session);
        // Send welcome asking for name
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "welcome");
        msg.addProperty("id", id);
        session.sendMessage(new TextMessage(new Gson().toJson(msg)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();
        JsonObject msg = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        String type = msg.has("type") ? msg.get("type").getAsString() : "";

        switch (type) {
            case "join" -> {
                String name = msg.has("name") ? msg.get("name").getAsString() : "Player";
                engine.addPlayer(id, name);
            }
            case "dir" -> {
                String dir = msg.get("dir").getAsString();
                engine.setDirection(id, dir);
            }
            case "respawn" -> engine.respawn(id);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        sessions.remove(id);
        engine.removePlayer(id);
    }
}
