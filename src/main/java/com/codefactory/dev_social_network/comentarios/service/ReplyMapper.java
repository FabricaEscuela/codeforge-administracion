package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.dto.ReplyResponse;

import java.util.Map;
import java.util.UUID;

final class ReplyMapper {

    private ReplyMapper() {
    }

    static ReplyResponse toResponse(CommentReply reply, Map<UUID, String> usernames) {
        return new ReplyResponse(reply.id(), reply.commentId(), reply.userId(),
                usernames.get(reply.userId()), reply.content(), reply.createdAt());
    }
}
