package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.interfaces.CommentLookupPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Regla compartida por los casos de uso de HU014: el proyecto debe existir, el comentario
 * debe existir y el comentario debe pertenecer a ese proyecto.
 */
@Component
public class CommentInProjectGuard {

    private final ProyectoRefRepository proyectoRefRepository;
    private final CommentLookupPort commentLookup;

    public CommentInProjectGuard(ProyectoRefRepository proyectoRefRepository, CommentLookupPort commentLookup) {
        this.proyectoRefRepository = proyectoRefRepository;
        this.commentLookup = commentLookup;
    }

    public void ensureProjectExists(UUID projectId) {
        if (!proyectoRefRepository.existsById(projectId)) {
            throw new BusinessException(
                    "PROJECT_NOT_FOUND", "El proyecto no existe", HttpStatus.NOT_FOUND);
        }
    }

    public void ensureCommentBelongsToProject(UUID projectId, UUID commentId) {
        ensureProjectExists(projectId);
        UUID commentProjectId = commentLookup.findProjectIdByCommentId(commentId)
                .orElseThrow(() -> new BusinessException(
                        "COMMENT_NOT_FOUND", "El comentario no existe", HttpStatus.NOT_FOUND));
        if (!commentProjectId.equals(projectId)) {
            throw new BusinessException(
                    "COMMENT_NOT_IN_PROJECT", "El comentario no pertenece a este proyecto", HttpStatus.NOT_FOUND);
        }
    }
}
