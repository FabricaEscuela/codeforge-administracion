package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.dto.CommentRepliesResponse;

import java.util.UUID;

public interface ListCommentRepliesUseCase {
    CommentRepliesResponse list(UUID projectId, UUID commentId);
}

