package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.entity.ProjectCommentEntity;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProjectCommentRepositoryAdapter implements ProjectCommentRepositoryPort {

    private final ProjectCommentJpaRepository repository;

    public ProjectCommentRepositoryAdapter(ProjectCommentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProjectComment save(ProjectComment comment) {
        ProjectCommentEntity saved = repository.save(new ProjectCommentEntity(
                comment.projectId(), comment.userId(), comment.content()));
        return toDomain(saved);
    }

    @Override
    public List<ProjectComment> findByProjectId(UUID projectId) {
        return repository.findByProjectIdOrderByCreatedAtAsc(projectId).stream()
                .map(ProjectCommentRepositoryAdapter::toDomain)
                .toList();
    }

    private static ProjectComment toDomain(ProjectCommentEntity entity) {
        return new ProjectComment(entity.getId(), entity.getProjectId(), entity.getUserId(),
                entity.getContent(), entity.getCreatedAt());
    }
}
