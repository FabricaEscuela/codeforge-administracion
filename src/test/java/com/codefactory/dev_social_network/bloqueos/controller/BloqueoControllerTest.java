package com.codefactory.dev_social_network.bloqueos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.dto.BloquearUsuarioRequest;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloquearUsuarioUseCase;
import com.codefactory.dev_social_network.bloqueos.interfaces.DesbloquearUsuarioUseCase;

@ExtendWith(MockitoExtension.class)
class BloqueoControllerTest {

    @Mock
    private BloquearUsuarioUseCase bloquearUsuarioUseCase;

    @Mock
    private DesbloquearUsuarioUseCase desbloquearUsuarioUseCase;

    @InjectMocks
    private BloqueoController controller;

    @Test
    @DisplayName("Convierte un usuarioId válido a UUID y responde 201")
    void convierteUnUuidValido() {
        // Arrange
        UUID blockerId = UUID.randomUUID();
        UUID bloqueadoId = UUID.randomUUID();
        BloquearUsuarioRequest request = new BloquearUsuarioRequest(bloqueadoId.toString());
        when(bloquearUsuarioUseCase.bloquear(any(UUID.class), any(UUID.class)))
                .thenReturn(new BloqueoResponse("Usuario bloqueado exitosamente"));

        // Act
        ResponseEntity<BloqueoResponse> respuesta = controller.bloquear(blockerId, request);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isNotNull();
        assertThat(respuesta.getBody().getMensaje()).isEqualTo("Usuario bloqueado exitosamente");
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(bloquearUsuarioUseCase).bloquear(
                org.mockito.ArgumentMatchers.eq(blockerId), captor.capture());
        assertThat(captor.getValue()).isEqualTo(bloqueadoId);
    }

    @ParameterizedTest
    @ValueSource(strings = { "9999", "no-es-uuid" })
    @NullAndEmptySource
    @DisplayName("Envía null al caso de uso cuando el usuarioId no es un UUID")
    void enviaNullSiElUsuarioIdNoEsUuid(String usuarioId) {
        // Arrange
        UUID blockerId = UUID.randomUUID();
        BloquearUsuarioRequest request = new BloquearUsuarioRequest(usuarioId);
        when(bloquearUsuarioUseCase.bloquear(any(UUID.class), isNull()))
                .thenReturn(new BloqueoResponse("Usuario bloqueado exitosamente"));

        // Act
        controller.bloquear(blockerId, request);

        // Assert
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(bloquearUsuarioUseCase).bloquear(
                org.mockito.ArgumentMatchers.eq(blockerId), captor.capture());
        assertThat(captor.getValue()).isNull();
    }
}
