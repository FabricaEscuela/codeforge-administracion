package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.LikedProjectResponse;
import com.codefactory.dev_social_network.proyectos.entity.ProjectLike;
import com.codefactory.dev_social_network.proyectos.entity.ProjectLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, ProjectLikeId> {

    boolean existsByProjectIdAndUsuarioId(UUID projectId, UUID usuarioId);

    long deleteByProjectIdAndUsuarioId(UUID projectId, UUID usuarioId);

    long countByProjectId(UUID projectId);

    @Query("select new com.codefactory.dev_social_network.proyectos.dto.LikedProjectResponse("
            + "p.id, p.title, p.repositoryUrl, l.createdAt) "
            + "from ProjectLike l, Project p "
            + "where p.id = l.projectId and l.usuarioId = :usuarioId "
            + "order by l.createdAt desc")
    List<LikedProjectResponse> findLikedProjects(@Param("usuarioId") UUID usuarioId);
}