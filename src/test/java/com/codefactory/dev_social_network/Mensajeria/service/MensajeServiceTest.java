package com.codefactory.dev_social_network.Mensajeria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.Mensajeria.dto.EnviarMensajeRequest;
import com.codefactory.dev_social_network.Mensajeria.dto.MensajeResponse;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import com.codefactory.dev_social_network.Mensajeria.repository.MessageRepository;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

class MensajeServiceTest {

    private final MessageRepository messageRepository = mock(MessageRepository.class);
    private final UsuarioExistenciaPort usuarioExistencia = mock(UsuarioExistenciaPort.class);
    private final ConversationResolver conversationResolver = mock(ConversationResolver.class);
    private final BloqueoInteraccionPort bloqueoPolicy = mock(BloqueoInteraccionPort.class);

    private final MensajeService service = new MensajeService(
            messageRepository, usuarioExistencia, conversationResolver, bloqueoPolicy);

    private final UUID remitenteId = UUID.randomUUID();
    private final UUID destinatarioId = UUID.randomUUID();

    @Test
    @DisplayName("No envía el mensaje cuando existe un bloqueo en cualquier dirección")
    void envioBloqueado() {
        when(usuarioExistencia.existe(destinatarioId)).thenReturn(true);
        when(bloqueoPolicy.estaBloqueada(remitenteId, destinatarioId)).thenReturn(true);

        assertThatThrownBy(() -> service.enviarMensaje(remitenteId, request("Hola")))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("ENVIO_BLOQUEADO");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception.getMessage())
                            .isEqualTo("No es posible enviar mensajes a este usuario");
                });

        verify(messageRepository, never()).save(any(MessageEntity.class));
        verify(conversationResolver, never()).resolve(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Envía el mensaje cuando no existe bloqueo")
    void envioPermitido() {
        when(usuarioExistencia.existe(destinatarioId)).thenReturn(true);
        when(bloqueoPolicy.estaBloqueada(remitenteId, destinatarioId)).thenReturn(false);
        when(conversationResolver.resolve(remitenteId, destinatarioId))
                .thenReturn(new ConversationEntity(remitenteId, destinatarioId));

        MensajeResponse response = service.enviarMensaje(remitenteId, request("Hola"));

        assertThat(response.getMensaje()).isEqualTo("Mensaje enviado");
        verify(messageRepository).save(any(MessageEntity.class));
    }

    @Test
    @DisplayName("Valida el destinatario inexistente antes de evaluar el bloqueo")
    void destinatarioInexistente() {
        when(usuarioExistencia.existe(destinatarioId)).thenReturn(false);

        assertThatThrownBy(() -> service.enviarMensaje(remitenteId, request("Hola")))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("USUARIO_NO_EXISTE");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getMessage()).isEqualTo("Usuario no existe");
                });

        verify(bloqueoPolicy, never()).estaBloqueada(any(UUID.class), any(UUID.class));
        verify(messageRepository, never()).save(any(MessageEntity.class));
    }

    private EnviarMensajeRequest request(String contenido) {
        EnviarMensajeRequest request = new EnviarMensajeRequest();
        request.setDestinatarioId(destinatarioId);
        request.setContenido(contenido);
        return request;
    }
}
