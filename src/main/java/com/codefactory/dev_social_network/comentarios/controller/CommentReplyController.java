package com.codefactory.dev_social_network.comentarios.controller;

import com.codefactory.dev_social_network.comentarios.dto.CommentRepliesResponse;
import com.codefactory.dev_social_network.comentarios.dto.CreateReplyRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CountCommentRepliesUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ListCommentRepliesUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ReplyToCommentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/comments")
public class CommentReplyController {

    private final ReplyToCommentUseCase replyUseCase;
    private final ListCommentRepliesUseCase listRepliesUseCase;
    private final CountCommentRepliesUseCase countRepliesUseCase;

    public CommentReplyController(ReplyToCommentUseCase replyUseCase,
                                  ListCommentRepliesUseCase listRepliesUseCase,
                                  CountCommentRepliesUseCase countRepliesUseCase) {
        this.replyUseCase = replyUseCase;
        this.listRepliesUseCase = listRepliesUseCase;
        this.countRepliesUseCase = countRepliesUseCase;
    }

    @PostMapping("/{commentId}/replies")
    @Operation(summary = "Reply to a project comment (thread)")
    public ResponseEntity<PublishedReplyResponse> reply(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID projectId,
            @PathVariable UUID commentId,
            @Valid @RequestBody CreateReplyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(replyUseCase.reply(userId, projectId, commentId, request));
    }

    @GetMapping("/{commentId}/replies")
    @Operation(summary = "List the replies of a project comment")
    public ResponseEntity<CommentRepliesResponse> list(
            @PathVariable UUID projectId,
            @PathVariable UUID commentId
    ) {
        return ResponseEntity.ok(listRepliesUseCase.list(projectId, commentId));
    }

    @GetMapping("/reply-counts")
    @Operation(summary = "Reply totals per comment of a project")
    public ResponseEntity<Map<UUID, Long>> counts(@PathVariable UUID projectId) {
        return ResponseEntity.ok(countRepliesUseCase.countByProject(projectId));
    }
}
