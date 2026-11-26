package com.codefactory.dev_social_network.Mensajeria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversacionResolverPort;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

class IniciarConversacionServiceTest {

    private final BloqueoInteraccionPort bloqueoPolicy = mock(BloqueoInteraccionPort.class);
    private final UsuarioExistenciaPort usuarioExistencia = mock(UsuarioExistenciaPort.class);
    private final ConversacionResolverPort conversationResolver = mock(ConversacionResolverPort.class);

    private final IniciarConversacionService service =
            new IniciarConversacionService(bloqueoPolicy, usuarioExistencia, conversationResolver);

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID otroUsuarioId = UUID.randomUUID();

    @Test
    @DisplayName("No inicia la conversación cuando existe un bloqueo")
    void inicioBloqueado() {
        when(bloqueoPolicy.estaBloqueada(usuarioId, otroUsuarioId)).thenReturn(true);

        assertThatThrownBy(() -> service.iniciar(usuarioId, otroUsuarioId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("CONVERSACION_BLOQUEADA");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception.getMessage())
                            .isEqualTo("No es posible iniciar una conversación con este usuario");
                });

        verifyNoInteractions(usuarioExistencia);
        verify(conversationResolver, never()).find(any(UUID.class), any(UUID.class));
        verify(conversationResolver, never()).crear(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Rechaza iniciar conversación con un usuario inexistente")
    void usuarioInexistente() {
        when(bloqueoPolicy.estaBloqueada(usuarioId, otroUsuarioId)).thenReturn(false);
        when(usuarioExistencia.existe(otroUsuarioId)).thenReturn(false);

        assertThatThrownBy(() -> service.iniciar(usuarioId, otroUsuarioId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("USUARIO_NO_EXISTE");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getMessage()).isEqualTo("El usuario no existe");
                });

        verify(conversationResolver, never()).crear(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Devuelve la conversación existente sin crear otra")
    void conversacionExistente() {
        UUID conversacionId = UUID.randomUUID();
        ConversationEntity existente = mock(ConversationEntity.class);
        when(existente.getId()).thenReturn(conversacionId);
        when(bloqueoPolicy.estaBloqueada(usuarioId, otroUsuarioId)).thenReturn(false);
        when(usuarioExistencia.existe(otroUsuarioId)).thenReturn(true);
        when(conversationResolver.find(usuarioId, otroUsuarioId)).thenReturn(Optional.of(existente));

        InicioConversacionResultado resultado = service.iniciar(usuarioId, otroUsuarioId);

        assertThat(resultado.creada()).isFalse();
        assertThat(resultado.conversacion().conversacionId()).isEqualTo(conversacionId);
        verify(conversationResolver, never()).crear(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Crea la conversación cuando no existe")
    void conversacionNueva() {
        UUID conversacionId = UUID.randomUUID();
        ConversationEntity creada = mock(ConversationEntity.class);
        when(creada.getId()).thenReturn(conversacionId);
        when(bloqueoPolicy.estaBloqueada(usuarioId, otroUsuarioId)).thenReturn(false);
        when(usuarioExistencia.existe(otroUsuarioId)).thenReturn(true);
        when(conversationResolver.find(usuarioId, otroUsuarioId)).thenReturn(Optional.empty());
        when(conversationResolver.crear(usuarioId, otroUsuarioId)).thenReturn(creada);

        InicioConversacionResultado resultado = service.iniciar(usuarioId, otroUsuarioId);

        assertThat(resultado.creada()).isTrue();
        assertThat(resultado.conversacion().conversacionId()).isEqualTo(conversacionId);
        verify(conversationResolver).crear(usuarioId, otroUsuarioId);
    }

    @Test
    @DisplayName("Rechaza iniciar conversación cuando el identificador no es válido")
    void identificadorInvalido() {
        assertThatThrownBy(() -> service.iniciar(usuarioId, null))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("USUARIO_NO_EXISTE");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getMessage()).isEqualTo("El usuario no existe");
                });

        verifyNoInteractions(usuarioExistencia);
        verify(conversationResolver, never()).find(any(UUID.class), any(UUID.class));
        verify(conversationResolver, never()).crear(any(UUID.class), any(UUID.class));
    }
}
