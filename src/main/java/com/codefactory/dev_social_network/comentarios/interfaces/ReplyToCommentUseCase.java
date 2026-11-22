package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.dto.CreateReplyRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedReplyResponse;

import java.util.UUID;

public interface ReplyToCommentUseCase {
    PublishedReplyResponse reply(UUID userId, UUID projectId, UUID commentId, CreateReplyRequest request);
}