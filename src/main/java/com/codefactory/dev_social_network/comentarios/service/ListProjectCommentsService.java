package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.dto.CommentResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentVotePort;
import com.codefactory.dev_social_network.comentarios.interfaces.ListProjectCommentsUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListProjectCommentsService implements ListProjectCommentsUseCase {

    private final ProyectoRefRepository proyectoRefRepository;
    private final ProjectCommentRepositoryPort commentRepository;
    private final CommentAuthorDirectoryPort authorDirectory;
    private final CommentVotePort votePort;

    public ListProjectCommentsService(ProyectoRefRepository proyectoRefRepository,
                                      ProjectCommentRepositoryPort commentRepository,
                                      CommentAuthorDirectoryPort authorDirectory,
                                      CommentVotePort votePort) {
        this.proyectoRefRepository = proyectoRefRepository;
        this.commentRepository = commentRepository;
        this.authorDirectory = authorDirectory;
        this.votePort = votePort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> list(UUID projectId, UUID userId) {
        if (!proyectoRefRepository.existsById(projectId)) {
            throw new BusinessException(
                    "PROJECT_NOT_FOUND", "El proyecto no existe", HttpStatus.NOT_FOUND);
        }

        List<ProjectComment> comments = commentRepository.findByProjectId(projectId);
        Set<UUID> userIds = comments.stream().map(ProjectComment::userId).collect(Collectors.toSet());
        Map<UUID, String> usernames = authorDirectory.usernamesFor(userIds);
        Set<UUID> commentIds = comments.stream().map(ProjectComment::id).collect(Collectors.toSet());
        Map<UUID, Long> votes = votePort.contarVotos(commentIds);
        Set<UUID> votedByMe = votePort.comentariosVotadosPor(userId, commentIds);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.id(), comment.userId(),
                        usernames.get(comment.userId()), comment.content(), comment.createdAt(),
                        votes.getOrDefault(comment.id(), 0L), votedByMe.contains(comment.id())))
                .toList();
    }
}