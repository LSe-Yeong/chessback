package com.example.chessback.chess.dto;

public record StatusDto(
        String roomId,
        String status,
        String winner
) {
}
