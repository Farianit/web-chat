package com.trackensure.testtask.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocket")
public class MessageWebSocket {
    private static final Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void open(Session session) {
        sessions.add(session);
    }

    /**
     * Send message to all active WebSocket clients.
     * @param message Message text
     */
    public static void sendMessage(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void close(Session session) {
        try {
            session.close();
            sessions.remove(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        try {
            session.close();
            sessions.remove(session);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
