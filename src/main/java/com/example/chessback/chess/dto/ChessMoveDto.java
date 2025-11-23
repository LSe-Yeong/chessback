package com.example.chessback.chess.dto;

import lombok.Builder;

@Builder
public record ChessMoveDto(
        String roomId,
        String name,
        String color,
        Integer beforeRow,
        Integer beforeCol,
        Integer afterRow,
        Integer afterCol,
        String nextTurn
) {
}
