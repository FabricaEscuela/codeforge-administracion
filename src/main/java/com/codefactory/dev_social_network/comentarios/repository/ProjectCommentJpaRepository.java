package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.entity.ProjectCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectCommentJpaRepository extends JpaRepository<ProjectCommentEntity, UUID> {
    List<ProjectCommentEntity> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
