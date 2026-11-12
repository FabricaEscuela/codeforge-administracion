package com.codefactory.dev_social_network.comentarios.interfaces;

import com.codefactory.dev_social_network.comentarios.dto.CommentVoteResponse;

import java.util.UUID;

public interface VoteCommentUseCase {

    CommentVoteResponse vote(UUID userId, UUID projectId, UUID commentId);

    CommentVoteResponse removeVote(UUID userId, UUID projectId, UUID commentId);
}