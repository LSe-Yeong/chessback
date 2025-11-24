package com.example.chessback.global.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        messagingTemplate.convertAndSend(
                "/sub/chess/leave",
                "LEAVE"
        );
    }
}
