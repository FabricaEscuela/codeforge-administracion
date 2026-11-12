package com.codefactory.dev_social_network.comentarios.controller;

import com.codefactory.dev_social_network.comentarios.dto.CommentVoteResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.VoteCommentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/comments/{commentId}/votes")
@Tag(name = "Comment Votes", description = "Votes on project comments")
public class CommentVoteController {

    private final VoteCommentUseCase voteCommentUseCase;

    public CommentVoteController(VoteCommentUseCase voteCommentUseCase) {
        this.voteCommentUseCase = voteCommentUseCase;
    }

    @PostMapping
    @Operation(summary = "Vote a comment",
            description = "Toggles the authenticated user's positive vote: registers it (201) or removes it if it already existed (200)")
    public ResponseEntity<CommentVoteResponse> vote(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID projectId,
            @PathVariable UUID commentId
    ) {
        CommentVoteResponse response = voteCommentUseCase.vote(userId, projectId, commentId);
        return ResponseEntity.status(response.votadoPorMi() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping
    @Operation(summary = "Remove my vote from a comment",
            description = "Removes the authenticated user's vote; idempotent")
    public ResponseEntity<CommentVoteResponse> removeVote(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID projectId,
            @PathVariable UUID commentId
    ) {
        return ResponseEntity.ok(voteCommentUseCase.removeVote(userId, projectId, commentId));
    }
}