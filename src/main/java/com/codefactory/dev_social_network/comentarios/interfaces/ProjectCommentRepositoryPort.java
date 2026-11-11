package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;

import java.util.List;
import java.util.UUID;

public interface ProjectCommentRepositoryPort {
    ProjectComment save(ProjectComment comment);

    List<ProjectComment> findByProjectId(UUID projectId);
}
