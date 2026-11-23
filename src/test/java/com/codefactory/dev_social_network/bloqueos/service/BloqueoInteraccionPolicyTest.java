package com.codefactory.dev_social_network.bloqueos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;

class BloqueoInteraccionPolicyTest {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository =
            mock(UsuarioBloqueoRepositoryPort.class);
    private final BloqueoInteraccionPolicy policy = new BloqueoInteraccionPolicy(bloqueoRepository);

    private final UUID usuarioA = UUID.randomUUID();
    private final UUID usuarioB = UUID.randomUUID();

    @Test
    @DisplayName("Detecta el bloqueo cuando A bloqueó a B")
    void bloqueoDirecto() {
        when(bloqueoRepository.existe(usuarioA, usuarioB)).thenReturn(true);

        assertThat(policy.estaBloqueada(usuarioA, usuarioB)).isTrue();
    }

    @Test
    @DisplayName("Detecta el bloqueo cuando B bloqueó a A")
    void bloqueoInverso() {
        when(bloqueoRepository.existe(usuarioA, usuarioB)).thenReturn(false);
        when(bloqueoRepository.existe(usuarioB, usuarioA)).thenReturn(true);

        assertThat(policy.estaBloqueada(usuarioA, usuarioB)).isTrue();
    }

    @Test
    @DisplayName("No hay bloqueo en ninguna dirección")
    void sinBloqueo() {
        when(bloqueoRepository.existe(usuarioA, usuarioB)).thenReturn(false);
        when(bloqueoRepository.existe(usuarioB, usuarioA)).thenReturn(false);

        assertThat(policy.estaBloqueada(usuarioA, usuarioB)).isFalse();
    }

    @Test
    @DisplayName("No consulta el repositorio cuando falta un usuario o son el mismo")
    void idsInvalidosNoConsultados() {
        assertThat(policy.estaBloqueada(null, usuarioB)).isFalse();
        assertThat(policy.estaBloqueada(usuarioA, null)).isFalse();
        assertThat(policy.estaBloqueada(usuarioA, usuarioA)).isFalse();

        verifyNoInteractions(bloqueoRepository);
    }
}
