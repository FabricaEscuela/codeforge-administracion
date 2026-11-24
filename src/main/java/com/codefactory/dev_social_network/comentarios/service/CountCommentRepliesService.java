package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CountCommentRepliesUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class CountCommentRepliesService implements CountCommentRepliesUseCase {

    private final CommentInProjectGuard guard;
    private final CommentReplyRepositoryPort replyRepository;

    public CountCommentRepliesService(CommentInProjectGuard guard, CommentReplyRepositoryPort replyRepository) {
        this.guard = guard;
        this.replyRepository = replyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Long> countByProject(UUID projectId) {
        guard.ensureProjectExists(projectId);
        return replyRepository.countByProjectId(projectId);
    }
}


