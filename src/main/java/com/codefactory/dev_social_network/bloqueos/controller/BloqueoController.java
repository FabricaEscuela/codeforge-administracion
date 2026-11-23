package com.codefactory.dev_social_network.bloqueos.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.dto.BloquearUsuarioRequest;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloquearUsuarioUseCase;
import com.codefactory.dev_social_network.bloqueos.interfaces.DesbloquearUsuarioUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bloqueos")
@Tag(
        name = "Bloqueos",
        description = "Operaciones para bloquear y desbloquear usuarios"
)
public class BloqueoController {

    private final BloquearUsuarioUseCase bloquearUsuarioUseCase;
    private final DesbloquearUsuarioUseCase desbloquearUsuarioUseCase;

    public BloqueoController(BloquearUsuarioUseCase bloquearUsuarioUseCase,
                             DesbloquearUsuarioUseCase desbloquearUsuarioUseCase) {
        this.bloquearUsuarioUseCase = bloquearUsuarioUseCase;
        this.desbloquearUsuarioUseCase = desbloquearUsuarioUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Bloquear a un usuario",
            description = "Registra un bloqueo del usuario autenticado hacia el usuario indicado"
    )
    public ResponseEntity<BloqueoResponse> bloquear(
            @AuthenticationPrincipal UUID blockerId,
            @Valid @RequestBody BloquearUsuarioRequest request
    ) {
        BloqueoResponse response = bloquearUsuarioUseCase.bloquear(blockerId, request.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{usuarioId}")
    @Operation(
            summary = "Desbloquear a un usuario",
            description = "Elimina el bloqueo del usuario autenticado hacia el usuario indicado"
    )
    public ResponseEntity<BloqueoResponse> desbloquear(
            @AuthenticationPrincipal UUID blockerId,
            @PathVariable UUID usuarioId
    ) {
        return ResponseEntity.ok(desbloquearUsuarioUseCase.desbloquear(blockerId, usuarioId));
    }
}
