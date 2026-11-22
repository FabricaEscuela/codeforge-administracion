package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.dto.CreateReplyRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedReplyResponse;
import com.codefactory.dev_social_network.comentarios.dto.ReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.ReplyToCommentUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ReplyToCommentService implements ReplyToCommentUseCase {

    private final CommentInProjectGuard guard;
    private final CommentReplyRepositoryPort replyRepository;
    private final CommentAuthorDirectoryPort authorDirectory;

    public ReplyToCommentService(CommentInProjectGuard guard,
                                 CommentReplyRepositoryPort replyRepository,
                                 CommentAuthorDirectoryPort authorDirectory) {
        this.guard = guard;
        this.replyRepository = replyRepository;
        this.authorDirectory = authorDirectory;
    }

    @Override
    @Transactional
    public PublishedReplyResponse reply(UUID userId, UUID projectId, UUID commentId, CreateReplyRequest request) {
        guard.ensureCommentBelongsToProject(projectId, commentId);

        CommentReply saved = replyRepository.save(
                new CommentReply(null, commentId, userId, request.respuesta().trim(), null));
        Map<UUID, String> usernames = authorDirectory.usernamesFor(Set.of(userId));
        long total = replyRepository.countByCommentId(commentId);

        return new PublishedReplyResponse("Respuesta publicada", ReplyMapper.toResponse(saved, usernames), total);
    }
}
