package com.codefactory.dev_social_network.comentarios.controller;

import com.codefactory.dev_social_network.comentarios.dto.CommentResponse;
import com.codefactory.dev_social_network.comentarios.dto.CreateCommentRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedCommentResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CreateProjectCommentUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ListProjectCommentsUseCase;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/comments")
public class ProjectCommentController {

    private final CreateProjectCommentUseCase createCommentUseCase;
    private final ListProjectCommentsUseCase listCommentsUseCase;

    public ProjectCommentController(CreateProjectCommentUseCase createCommentUseCase,
                                    ListProjectCommentsUseCase listCommentsUseCase) {
        this.createCommentUseCase = createCommentUseCase;
        this.listCommentsUseCase = listCommentsUseCase;
    }

    @PostMapping
    @Operation(summary = "Comment on a project")
    public ResponseEntity<PublishedCommentResponse> create(
            @AuthenticationPrincipal UUID userId,
            @PathVariable Long projectId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createCommentUseCase.create(userId, projectId, request));
    }

    @GetMapping
    @Operation(summary = "List comments on a project")
    public ResponseEntity<List<CommentResponse>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(listCommentsUseCase.list(projectId));
    }
}
