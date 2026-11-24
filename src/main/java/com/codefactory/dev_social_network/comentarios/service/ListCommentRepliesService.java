package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.dto.CommentRepliesResponse;
import com.codefactory.dev_social_network.comentarios.dto.ReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.ListCommentRepliesUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListCommentRepliesService implements ListCommentRepliesUseCase {

    private final CommentInProjectGuard guard;
    private final CommentReplyRepositoryPort replyRepository;
    private final CommentAuthorDirectoryPort authorDirectory;

    public ListCommentRepliesService(CommentInProjectGuard guard,
                                     CommentReplyRepositoryPort replyRepository,
                                     CommentAuthorDirectoryPort authorDirectory) {
        this.guard = guard;
        this.replyRepository = replyRepository;
        this.authorDirectory = authorDirectory;
    }

    @Override
    @Transactional(readOnly = true)
    public CommentRepliesResponse list(UUID projectId, UUID commentId) {
        guard.ensureCommentBelongsToProject(projectId, commentId);

        List<CommentReply> replies = replyRepository.findByCommentId(commentId);
        Set<UUID> userIds = replies.stream().map(CommentReply::userId).collect(Collectors.toSet());
        Map<UUID, String> usernames = authorDirectory.usernamesFor(userIds);
        List<ReplyResponse> body = replies.stream()
                .map(reply -> ReplyMapper.toResponse(reply, usernames))
                .toList();
        return new CommentRepliesResponse(commentId, body.size(), body);
    }
}
