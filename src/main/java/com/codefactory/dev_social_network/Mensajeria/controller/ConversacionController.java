package com.codefactory.dev_social_network.Mensajeria.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversacionResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;
import com.codefactory.dev_social_network.Mensajeria.dto.IniciarConversacionRequest;
import com.codefactory.dev_social_network.Mensajeria.interfaces.IniciarConversacionUseCase;
import com.codefactory.dev_social_network.shared.web.UuidParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conversaciones")
@Tag(
        name = "Conversaciones",
        description = "Operaciones para iniciar conversaciones privadas"
)
public class ConversacionController {

    private final IniciarConversacionUseCase iniciarConversacionUseCase;

    public ConversacionController(IniciarConversacionUseCase iniciarConversacionUseCase) {
        this.iniciarConversacionUseCase = iniciarConversacionUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Iniciar una conversación privada",
            description = "Crea la conversación con el usuario indicado o devuelve la existente"
    )
    public ResponseEntity<ConversacionResponse> iniciar(
            @AuthenticationPrincipal UUID usuarioId,
            @Valid @RequestBody IniciarConversacionRequest request
    ) {
        UUID otroUsuarioId = UuidParser.parseOrNull(request.getUsuarioId());
        InicioConversacionResultado resultado = iniciarConversacionUseCase.iniciar(usuarioId, otroUsuarioId);

        return ResponseEntity
                .status(resultado.creada() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(resultado.conversacion());
    }
}
