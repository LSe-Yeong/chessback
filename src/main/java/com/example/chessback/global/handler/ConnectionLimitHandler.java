package com.example.chessback.global.handler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Component
public class ConnectionLimitHandler extends WebSocketHandlerDecorator {

    private static final int MAX_CONNECTIONS = 2;
    private final Set<String> sessionIds = ConcurrentHashMap.newKeySet();

    public ConnectionLimitHandler(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (sessionIds.size() >= MAX_CONNECTIONS) {
            session.close();
            return;
        }

        sessionIds.add(session.getId());
        System.out.println("CONNECTED → 현재 연결: " + sessionIds.size());
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      org.springframework.web.socket.CloseStatus closeStatus) throws Exception {
        sessionIds.remove(session.getId());
        System.out.println("DISCONNECTED → 현재 연결: " + sessionIds.size());
        super.afterConnectionClosed(session, closeStatus);
    }
}
