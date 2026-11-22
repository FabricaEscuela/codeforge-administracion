package com.codefactory.dev_social_network.comentarios.dto;

import java.util.UUID;

public record CommentVoteResponse(
        UUID commentId,
        long votos,
        boolean votadoPorMi,
        String mensaje
) {
}