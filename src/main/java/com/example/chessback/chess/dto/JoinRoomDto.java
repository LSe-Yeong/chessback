package com.example.chessback.chess.dto;

public record JoinRoomDto(
        String roomId,
        String nickname,
        String type
) {
}
