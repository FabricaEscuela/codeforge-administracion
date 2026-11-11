package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.dto.CommentResponse;
import com.codefactory.dev_social_network.comentarios.dto.CreateCommentRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedCommentResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CreateProjectCommentUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CreateProjectCommentService implements CreateProjectCommentUseCase {

    private final ProyectoRefRepository proyectoRefRepository;
    private final ProjectCommentRepositoryPort commentRepository;
    private final CommentAuthorDirectoryPort authorDirectory;

    public CreateProjectCommentService(ProyectoRefRepository proyectoRefRepository,
                                       ProjectCommentRepositoryPort commentRepository,
                                       CommentAuthorDirectoryPort authorDirectory) {
        this.proyectoRefRepository = proyectoRefRepository;
        this.commentRepository = commentRepository;
        this.authorDirectory = authorDirectory;
    }

    @Override
    @Transactional
    public PublishedCommentResponse create(UUID userId, UUID projectId, CreateCommentRequest request) {
        if (!proyectoRefRepository.existsById(projectId)) {
            throw new BusinessException(
                    "PROJECT_NOT_FOUND", "El proyecto no existe", HttpStatus.NOT_FOUND);
        }

        ProjectComment saved = commentRepository.save(
                new ProjectComment(null, projectId, userId, request.comentario().trim(), null));
        Map<UUID, String> usernames = authorDirectory.usernamesFor(Set.of(userId));
        CommentResponse response = toResponse(saved, usernames);
        return new PublishedCommentResponse("Comentario publicado", response);
    }

    private static CommentResponse toResponse(ProjectComment comment, Map<UUID, String> usernames) {
        return new CommentResponse(comment.id(), comment.userId(), usernames.get(comment.userId()),
                comment.content(), comment.createdAt());
    }
}
