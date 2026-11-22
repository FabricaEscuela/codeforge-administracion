package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.entity.ProjectCommentEntity;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentLookupPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CommentLookupAdapter implements CommentLookupPort {

    private final ProjectCommentJpaRepository commentRepository;

    public CommentLookupAdapter(ProjectCommentJpaRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<UUID> findProjectIdByCommentId(UUID commentId) {
        return commentRepository.findById(commentId).map(ProjectCommentEntity::getProjectId);
    }
}
