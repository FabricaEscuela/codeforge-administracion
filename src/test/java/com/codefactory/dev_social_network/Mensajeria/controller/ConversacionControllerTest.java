package com.codefactory.dev_social_network.Mensajeria.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.codefactory.dev_social_network.Mensajeria.dto.ConversacionResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;
import com.codefactory.dev_social_network.Mensajeria.dto.IniciarConversacionRequest;
import com.codefactory.dev_social_network.Mensajeria.interfaces.IniciarConversacionUseCase;

@ExtendWith(MockitoExtension.class)
class ConversacionControllerTest {

    @Mock
    private IniciarConversacionUseCase iniciarConversacionUseCase;

    @InjectMocks
    private ConversacionController controller;

    @Test
    @DisplayName("Convierte un usuarioId válido a UUID y responde 201 cuando la conversación es nueva")
    void responde201CuandoLaConversacionEsNueva() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID otroUsuarioId = UUID.randomUUID();
        UUID conversacionId = UUID.randomUUID();
        IniciarConversacionRequest request = new IniciarConversacionRequest(otroUsuarioId.toString());
        when(iniciarConversacionUseCase.iniciar(any(UUID.class), any(UUID.class)))
                .thenReturn(new InicioConversacionResultado(
                        new ConversacionResponse(conversacionId), true));

        // Act
        ResponseEntity<ConversacionResponse> respuesta = controller.iniciar(usuarioId, request);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isNotNull();
        assertThat(respuesta.getBody().conversacionId()).isEqualTo(conversacionId);
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(iniciarConversacionUseCase).iniciar(eq(usuarioId), captor.capture());
        assertThat(captor.getValue()).isEqualTo(otroUsuarioId);
    }

    @Test
    @DisplayName("Responde 200 cuando la conversación ya existía")
    void responde200CuandoLaConversacionExistia() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        UUID otroUsuarioId = UUID.randomUUID();
        UUID conversacionId = UUID.randomUUID();
        IniciarConversacionRequest request = new IniciarConversacionRequest(otroUsuarioId.toString());
        when(iniciarConversacionUseCase.iniciar(any(UUID.class), any(UUID.class)))
                .thenReturn(new InicioConversacionResultado(
                        new ConversacionResponse(conversacionId), false));

        // Act
        ResponseEntity<ConversacionResponse> respuesta = controller.iniciar(usuarioId, request);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).isNotNull();
        assertThat(respuesta.getBody().conversacionId()).isEqualTo(conversacionId);
    }

    @ParameterizedTest
    @ValueSource(strings = { "9999", "no-es-uuid" })
    @NullAndEmptySource
    @DisplayName("Envía null al caso de uso cuando el usuarioId no es un UUID")
    void enviaNullSiElUsuarioIdNoEsUuid(String otroUsuarioId) {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        IniciarConversacionRequest request = new IniciarConversacionRequest(otroUsuarioId);
        when(iniciarConversacionUseCase.iniciar(any(UUID.class), isNull()))
                .thenReturn(new InicioConversacionResultado(
                        new ConversacionResponse(UUID.randomUUID()), true));

        // Act
        controller.iniciar(usuarioId, request);

        // Assert
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(iniciarConversacionUseCase).iniciar(eq(usuarioId), captor.capture());
        assertThat(captor.getValue()).isNull();
    }
}
