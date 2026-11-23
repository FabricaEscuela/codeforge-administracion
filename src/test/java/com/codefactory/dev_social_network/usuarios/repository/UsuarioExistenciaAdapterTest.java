package com.codefactory.dev_social_network.usuarios.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsuarioExistenciaAdapterTest {

    private final UsuarioJpaRepository jpaRepository = mock(UsuarioJpaRepository.class);
    private final UsuarioExistenciaAdapter adapter = new UsuarioExistenciaAdapter(jpaRepository);

    private final UUID usuarioId = UUID.randomUUID();

    @Test
    @DisplayName("existe delega en existsById")
    void existeDelegado() {
        when(jpaRepository.existsById(usuarioId)).thenReturn(true);

        assertThat(adapter.existe(usuarioId)).isTrue();
    }

    @Test
    @DisplayName("existe devuelve false y no consulta cuando el id es nulo")
    void idNulo() {
        assertThat(adapter.existe(null)).isFalse();
        verifyNoInteractions(jpaRepository);
    }
}
