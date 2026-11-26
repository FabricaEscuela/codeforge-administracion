package com.codefactory.dev_social_network.Mensajeria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacySendMessageRequest;
import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import com.codefactory.dev_social_network.Mensajeria.repository.MessageRepository;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.UserFinderByEmailPort;

class SendLegacyMessageServiceTest {

    private final UserFinderByEmailPort userFinder = mock(UserFinderByEmailPort.class);
    private final ConversationResolver conversationResolver = mock(ConversationResolver.class);
    private final MessageRepository messageRepository = mock(MessageRepository.class);
    private final BloqueoInteraccionPort bloqueoPolicy = mock(BloqueoInteraccionPort.class);

    private final SendLegacyMessageService service = new SendLegacyMessageService(
            userFinder, conversationResolver, messageRepository, bloqueoPolicy);

    private final UUID remitenteId = UUID.randomUUID();
    private final UUID destinatarioId = UUID.randomUUID();
    private final LegacySendMessageRequest request =
            new LegacySendMessageRequest("maria@correo.com", "Hola");

    @Test
    @DisplayName("El envío legacy no persiste nada cuando existe un bloqueo")
    void envioLegacyBloqueado() {
        when(userFinder.buscarPorEmail("maria@correo.com"))
                .thenReturn(Optional.of(new UserSummary(
                        destinatarioId, "maria", "Maria", "Dev", "maria@correo.com")));
        when(bloqueoPolicy.estaBloqueada(remitenteId, destinatarioId)).thenReturn(true);

        assertThatThrownBy(() -> service.send(remitenteId, request))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("ENVIO_BLOQUEADO");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception.getMessage())
                            .isEqualTo("No es posible enviar mensajes a este usuario");
                });

        verify(messageRepository, never()).save(any(MessageEntity.class));
        verify(conversationResolver, never()).resolve(any(UUID.class), any(UUID.class));
    }
}
