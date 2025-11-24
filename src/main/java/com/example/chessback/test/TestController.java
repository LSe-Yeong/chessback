package com.example.chessback.test;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("request detected");
        return "Hello, World";
    }

    @MessageMapping("/chat/message")
    @SendTo("/sub/chat/room")
    public String stompTest() {
        System.out.println("STOMP detected");
        return "STOMP connect HELLO";
    }
}
