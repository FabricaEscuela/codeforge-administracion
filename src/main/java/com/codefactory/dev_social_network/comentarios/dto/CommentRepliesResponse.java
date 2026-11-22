package com.codefactory.dev_social_network.comentarios.dto;

import java.util.List;
import java.util.UUID;

public record CommentRepliesResponse(UUID idComentario, long totalRespuestas, List<ReplyResponse> respuestas) {
}

