package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.dto.CommentVoteResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentVotePort;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.VoteCommentUseCase;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VoteCommentService implements VoteCommentUseCase {

    private final ProyectoRefRepository proyectoRefRepository;
    private final ProjectCommentRepositoryPort commentRepository;
    private final CommentVotePort votePort;

    public VoteCommentService(ProyectoRefRepository proyectoRefRepository,
                              ProjectCommentRepositoryPort commentRepository,
                              CommentVotePort votePort) {
        this.proyectoRefRepository = proyectoRefRepository;
        this.commentRepository = commentRepository;
        this.votePort = votePort;
    }

    @Override
    @Transactional
    public CommentVoteResponse vote(UUID userId, UUID projectId, UUID commentId) {
        validate(projectId, commentId);
        // Interruptor: si el usuario ya habia votado, volver a votar retira su voto
        if (votePort.registrarVoto(commentId, userId)) {
            return new CommentVoteResponse(commentId, votePort.contarVotos(commentId), true, "Voto registrado");
        }
        votePort.eliminarVoto(commentId, userId);
        return new CommentVoteResponse(commentId, votePort.contarVotos(commentId), false, "Voto eliminado");
    }

    @Override
    @Transactional
    public CommentVoteResponse removeVote(UUID userId, UUID projectId, UUID commentId) {
        validate(projectId, commentId);
        votePort.eliminarVoto(commentId, userId);
        return new CommentVoteResponse(commentId, votePort.contarVotos(commentId), false, "Voto eliminado");
    }

    /** Orden de validación: proyecto, comentario, pertenencia del comentario al proyecto. */
    private void validate(UUID projectId, UUID commentId) {
        if (!proyectoRefRepository.existsById(projectId)) {
            throw new BusinessException("PROJECT_NOT_FOUND", "El proyecto no existe", HttpStatus.NOT_FOUND);
        }

        ProjectComment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new BusinessException("COMMENT_NOT_FOUND", "El comentario no existe", HttpStatus.NOT_FOUND));

        if (!comment.projectId().equals(projectId)) {
            throw new BusinessException("COMMENT_NOT_IN_PROJECT",
                    "El comentario no pertenece a este proyecto", HttpStatus.BAD_REQUEST);
        }
    }
}