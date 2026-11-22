package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.domain.CommentReplyCount;
import com.codefactory.dev_social_network.comentarios.entity.CommentReplyEntity;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CommentReplyRepositoryAdapter implements CommentReplyRepositoryPort {

    private final CommentReplyJpaRepository repository;

    public CommentReplyRepositoryAdapter(CommentReplyJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public CommentReply save(CommentReply reply) {
        CommentReplyEntity saved = repository.save(
                new CommentReplyEntity(reply.commentId(), reply.userId(), reply.content()));
        return toDomain(saved);
    }

    @Override
    public List<CommentReply> findByCommentId(UUID commentId) {
        return repository.findByCommentIdOrderByCreatedAtAsc(commentId).stream()
                .map(CommentReplyRepositoryAdapter::toDomain)
                .toList();
    }

    @Override
    public long countByCommentId(UUID commentId) {
        return repository.countByCommentId(commentId);
    }

    @Override
    public Map<UUID, Long> countByProjectId(UUID projectId) {
        Map<UUID, Long> totals = new HashMap<>();
        for (CommentReplyCount row : repository.countGroupedByProject(projectId)) {
            totals.put(row.commentId(), row.total());
        }
        return totals;
    }

    private static CommentReply toDomain(CommentReplyEntity entity) {
        return new CommentReply(entity.getId(), entity.getCommentId(), entity.getUserId(),
                entity.getContent(), entity.getCreatedAt());
    }
}