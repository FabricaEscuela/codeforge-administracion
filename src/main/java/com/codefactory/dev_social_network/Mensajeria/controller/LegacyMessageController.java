package com.codefactory.dev_social_network.Mensajeria.controller;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacyMessageResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.LegacySendMessageRequest;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListLegacyConversationMessagesUseCase;
import com.codefactory.dev_social_network.Mensajeria.interfaces.SendLegacyMessageUseCase;
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
@RequestMapping("/api/v1")
public class LegacyMessageController {

    private final SendLegacyMessageUseCase sendMessageUseCase;
    private final ListLegacyConversationMessagesUseCase listMessagesUseCase;

    public LegacyMessageController(SendLegacyMessageUseCase sendMessageUseCase,
                                   ListLegacyConversationMessagesUseCase listMessagesUseCase) {
        this.sendMessageUseCase = sendMessageUseCase;
        this.listMessagesUseCase = listMessagesUseCase;
    }

    @PostMapping("/messages")
    public ResponseEntity<LegacyMessageResponse> send(
            @AuthenticationPrincipal UUID senderId,
            @Valid @RequestBody LegacySendMessageRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sendMessageUseCase.send(senderId, request));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<LegacyMessageResponse>> list(
            @AuthenticationPrincipal UUID requesterId,
            @PathVariable UUID conversationId
    ) {
        return ResponseEntity.ok(listMessagesUseCase.list(requesterId, conversationId));
    }
}
