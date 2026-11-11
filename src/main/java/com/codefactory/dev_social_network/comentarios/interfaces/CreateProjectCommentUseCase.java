package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.dto.CreateCommentRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedCommentResponse;

import java.util.UUID;

public interface CreateProjectCommentUseCase {
    PublishedCommentResponse create(UUID userId, UUID projectId, CreateCommentRequest request);
}
