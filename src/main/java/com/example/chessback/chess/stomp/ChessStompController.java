package com.example.chessback.chess.stomp;

import com.example.chessback.chess.dto.ChessMoveDto;
import com.example.chessback.chess.dto.JoinRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChessStompController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chess/move")
    public void moveChessPiece(@RequestBody ChessMoveDto chessMoveDto) {
        System.out.println("chessMoveDto = " + chessMoveDto.toString());
        messagingTemplate.convertAndSend(
                "/sub/chess/move/" + chessMoveDto.roomId(),
                chessMoveDto
        );
    }

    @MessageMapping("/chess/leave")
    public void leaveChessRoom(String roomId) {
        System.out.println("leave detected");
        messagingTemplate.convertAndSend(
                "/sub/chess/leave/" + roomId,
                "LEAVE"
        );
    }

    @MessageMapping("/chess/join")
    public void joinChessRoom(@RequestBody JoinRoomDto joinRoomDto) {
        System.out.println("join detected");
        String type = "";
        if (joinRoomDto.type().equals("JOIN")) {
            type = "WAITING";
        } else {
            type = "JOIN";
        }

        messagingTemplate.convertAndSend(
                "/sub/chess/join/" + joinRoomDto.roomId() + "/" + type,
                joinRoomDto
        );
    }
}
