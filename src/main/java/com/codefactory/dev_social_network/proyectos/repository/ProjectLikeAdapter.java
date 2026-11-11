package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.LikedProjectResponse;
import com.codefactory.dev_social_network.proyectos.entity.ProjectLike;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectLikePort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProjectLikeAdapter implements ProjectLikePort {

    private final ProjectLikeRepository projectLikeRepository;

    public ProjectLikeAdapter(ProjectLikeRepository projectLikeRepository) {
        this.projectLikeRepository = projectLikeRepository;
    }

    @Override
    public boolean registrarLike(UUID projectId, UUID usuarioId) {
        if (projectLikeRepository.existsByProjectIdAndUsuarioId(projectId, usuarioId)) {
            return false;
        }
        try {
            projectLikeRepository.saveAndFlush(new ProjectLike(projectId, usuarioId));
            return true;
        } catch (DataIntegrityViolationException e) {
            // Dos peticiones simultáneas: la clave primaria evita el duplicado
            return false;
        }
    }

    @Override
    public boolean eliminarLike(UUID projectId, UUID usuarioId) {
        return projectLikeRepository.deleteByProjectIdAndUsuarioId(projectId, usuarioId) > 0;
    }

    @Override
    public long contarLikes(UUID projectId) {
        return projectLikeRepository.countByProjectId(projectId);
    }

    @Override
    public List<LikedProjectResponse> listarPorUsuario(UUID usuarioId) {
        return projectLikeRepository.findLikedProjects(usuarioId);
    }
}