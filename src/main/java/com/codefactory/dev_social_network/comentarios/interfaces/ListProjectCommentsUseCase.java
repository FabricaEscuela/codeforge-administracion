package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.dto.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface ListProjectCommentsUseCase {
    List<CommentResponse> list(UUID projectId);
}
