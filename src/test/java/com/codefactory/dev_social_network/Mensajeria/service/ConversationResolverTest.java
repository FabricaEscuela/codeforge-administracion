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

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.repository.ConversationRepository;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

class ConversationResolverTest {

    private final ConversationRepository repository = mock(ConversationRepository.class);
    private final BloqueoInteraccionPort bloqueoPolicy = mock(BloqueoInteraccionPort.class);

    private final ConversationResolver resolver =
            new ConversationResolver(repository, bloqueoPolicy);

    private final UUID usuarioA = UUID.randomUUID();
    private final UUID usuarioB = UUID.randomUUID();

    @Test
    @DisplayName("No crea la conversación cuando existe un bloqueo")
    void resolucionBloqueada() {
        when(bloqueoPolicy.estaBloqueada(usuarioA, usuarioB)).thenReturn(true);

        assertThatThrownBy(() -> resolver.resolve(usuarioA, usuarioB))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("CONVERSACION_BLOQUEADA");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception.getMessage())
                            .isEqualTo("No es posible iniciar una conversación con este usuario");
                });

        verify(repository, never()).save(any(ConversationEntity.class));
    }

    @Test
    @DisplayName("Devuelve la conversación existente sin duplicarla")
    void devuelveConversacionExistente() {
        ConversationEntity existente = mock(ConversationEntity.class);
        when(bloqueoPolicy.estaBloqueada(usuarioA, usuarioB)).thenReturn(false);
        when(repository.findByParticipantOneIdAndParticipantTwoId(any(UUID.class), any(UUID.class)))
                .thenReturn(Optional.of(existente));

        ConversationEntity resultado = resolver.resolve(usuarioA, usuarioB);

        assertThat(resultado).isSameAs(existente);
        verify(repository, never()).save(any(ConversationEntity.class));
    }

    @Test
    @DisplayName("Crea la conversación normalizando el orden de participantes")
    void creaConversacionNormalizada() {
        UUID menor = usuarioA.compareTo(usuarioB) <= 0 ? usuarioA : usuarioB;
        UUID mayor = menor.equals(usuarioA) ? usuarioB : usuarioA;
        when(bloqueoPolicy.estaBloqueada(usuarioA, usuarioB)).thenReturn(false);
        when(repository.findByParticipantOneIdAndParticipantTwoId(menor, mayor))
                .thenReturn(Optional.empty());
        when(repository.save(any(ConversationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ConversationEntity creada = resolver.resolve(usuarioA, usuarioB);

        assertThat(creada.getParticipantOneId()).isEqualTo(menor);
        assertThat(creada.getParticipantTwoId()).isEqualTo(mayor);
    }
}
