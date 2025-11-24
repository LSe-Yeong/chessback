package com.example.chessback.global.handler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class StompEventListener {
    private final Map<String, Set<String>> roomSessions = new ConcurrentHashMap<>();
    private static final int MAX_CONNECTIONS_PER_ROOM = 2;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String roomId = accessor.getFirstNativeHeader("roomId");

        if (roomId == null) {
            roomId = "default";
        }

        roomSessions.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        Set<String> sessions = roomSessions.get(roomId);

        synchronized (sessions) {
            if (sessions.size() >= MAX_CONNECTIONS_PER_ROOM) {
                System.out.println("방이 가득 참 → 연결 거부: " + roomId);
                messagingTemplate.convertAndSend(
                        "/sub/chess/status/" + roomId,
                        "FULL"
                );

                return;
            }
            sessions.add(sessionId);
            System.out.println("방 입장: " + roomId + ", 현재 인원: " + sessions.size());
        }
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        roomSessions.forEach((roomId, sessions) -> {
            if (sessions.remove(sessionId)) {
                System.out.println("방 퇴장: " + roomId + ", 남은 인원: " + sessions.size());
            }
        });
    }
}
