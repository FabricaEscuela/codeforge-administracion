package com.codefactory.dev_social_network.Mensajeria.controller;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationSummary;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListMyConversationsUseCase;
import com.codefactory.dev_social_network.shared.config.CurrentUserPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mensajes")
@Tag(name = "Mensajería", description = "Operaciones para consultar conversaciones y enviar mensajes privados")
public class ConversationController {

    private final ListMyConversationsUseCase listMyConversationsUseCase;
    private final CurrentUserPort currentUserPort;

    public ConversationController(ListMyConversationsUseCase listMyConversationsUseCase,
                                  CurrentUserPort currentUserPort) {
        this.listMyConversationsUseCase = listMyConversationsUseCase;
        this.currentUserPort = currentUserPort;
    }

    @GetMapping("/conversaciones")
    @Operation(summary = "Listar mis conversaciones con su indicador de mensajes no leídos",
            description = "Cada conversación trae unreadCount: los mensajes recibidos que el usuario autenticado aún no ha leído")
    public ResponseEntity<List<ConversationSummary>> listarConversaciones() {
        return ResponseEntity.ok(listMyConversationsUseCase.listar(currentUserPort.currentUserId()));
    }
}