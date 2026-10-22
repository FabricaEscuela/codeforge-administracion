package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;

import java.util.List;

public interface ProjectCommentRepositoryPort {
    ProjectComment save(ProjectComment comment);

    List<ProjectComment> findByProjectId(Long projectId);
}
