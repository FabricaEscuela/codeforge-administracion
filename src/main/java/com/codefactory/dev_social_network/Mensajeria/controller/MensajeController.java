package com.codefactory.dev_social_network.Mensajeria.controller;

import com.codefactory.dev_social_network.Mensajeria.dto.EnviarMensajeRequest;
import com.codefactory.dev_social_network.Mensajeria.dto.MensajeResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListConversationUseCase;
import com.codefactory.dev_social_network.Mensajeria.service.MensajeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mensajes")
@Tag(
        name = "Mensajería",
        description = "Operaciones para consultar conversaciones y enviar mensajes privados"
)
public class MensajeController {

    private final MensajeService mensajeService;
    private final ListConversationUseCase listConversationUseCase;

    public MensajeController(MensajeService mensajeService,
                             ListConversationUseCase listConversationUseCase) {
        this.mensajeService = mensajeService;
        this.listConversationUseCase = listConversationUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Enviar un mensaje privado",
            description = "Envía un mensaje al usuario indicado por el desarrollador autenticado"
    )
    public ResponseEntity<MensajeResponse> enviarMensaje(
            @AuthenticationPrincipal UUID remitenteId,
            @Valid @RequestBody EnviarMensajeRequest request
    ) {
        MensajeResponse response =
                mensajeService.enviarMensaje(remitenteId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Consultar una conversación privada",
            description = "Devuelve solo los mensajes intercambiados entre el usuario autenticado y el usuario indicado"
    )
    public ResponseEntity<List<MessageConversationItem>> listarConversacion(
            @AuthenticationPrincipal UUID usuarioId,
            @RequestParam("with") UUID otroUsuarioId
    ) {
        return ResponseEntity.ok(listConversationUseCase.listar(usuarioId, otroUsuarioId));
    }
}
