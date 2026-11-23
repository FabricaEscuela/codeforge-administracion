package com.codefactory.dev_social_network.bloqueos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codefactory.dev_social_network.bloqueos.entity.UsuarioBloqueadoEntity;

class UsuarioBloqueoRepositoryAdapterTest {

    private final UsuarioBloqueadoJpaRepository jpaRepository =
            mock(UsuarioBloqueadoJpaRepository.class);
    private final UsuarioBloqueoRepositoryAdapter adapter =
            new UsuarioBloqueoRepositoryAdapter(jpaRepository);

    private final UUID blockerId = UUID.randomUUID();
    private final UUID blockedId = UUID.randomUUID();

    @Test
    @DisplayName("existe delega en existsByBlockerIdAndBlockedId")
    void existeDelegado() {
        when(jpaRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)).thenReturn(true);

        assertThat(adapter.existe(blockerId, blockedId)).isTrue();
        verify(jpaRepository).existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Test
    @DisplayName("guardar construye la entidad del par de usuarios")
    void guardarConstruyeEntidad() {
        adapter.guardar(blockerId, blockedId);

        verify(jpaRepository).save(any(UsuarioBloqueadoEntity.class));
    }

    @Test
    @DisplayName("eliminar devuelve false cuando no existe el bloqueo")
    void eliminarInexistente() {
        when(jpaRepository.findByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(Optional.empty());

        assertThat(adapter.eliminar(blockerId, blockedId)).isFalse();
        verify(jpaRepository, never()).delete(any(UsuarioBloqueadoEntity.class));
    }

    @Test
    @DisplayName("eliminar borra y devuelve true cuando el bloqueo existe")
    void eliminarExistente() {
        UsuarioBloqueadoEntity bloqueo = new UsuarioBloqueadoEntity(blockerId, blockedId);
        when(jpaRepository.findByBlockerIdAndBlockedId(blockerId, blockedId))
                .thenReturn(Optional.of(bloqueo));

        assertThat(adapter.eliminar(blockerId, blockedId)).isTrue();
        verify(jpaRepository).delete(bloqueo);
    }
}
