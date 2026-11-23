package com.codefactory.dev_social_network.bloqueos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

class BloquearUsuarioServiceTest {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository =
            mock(UsuarioBloqueoRepositoryPort.class);
    private final UsuarioExistenciaPort usuarioExistencia = mock(UsuarioExistenciaPort.class);
    private final BloquearUsuarioService service =
            new BloquearUsuarioService(bloqueoRepository, usuarioExistencia);

    private final UUID blockerId = UUID.randomUUID();
    private final UUID bloqueadoId = UUID.randomUUID();

    @Test
    @DisplayName("Bloquea un usuario existente y devuelve el mensaje exacto")
    void bloqueoExitoso() {
        when(usuarioExistencia.existe(bloqueadoId)).thenReturn(true);
        when(bloqueoRepository.existe(blockerId, bloqueadoId)).thenReturn(false);

        BloqueoResponse response = service.bloquear(blockerId, bloqueadoId);

        assertThat(response.getMensaje()).isEqualTo("Usuario bloqueado exitosamente");
        verify(bloqueoRepository).guardar(blockerId, bloqueadoId);
    }

    @Test
    @DisplayName("Rechaza el auto-bloqueo sin guardar nada")
    void autoBloqueoRechazado() {
        assertThatThrownBy(() -> service.bloquear(blockerId, blockerId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("AUTO_BLOQUEO");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(exception.getMessage())
                            .isEqualTo("No puedes bloquear tu propio usuario");
                });

        verify(bloqueoRepository, never()).guardar(any(UUID.class), any(UUID.class));
        verifyNoInteractions(usuarioExistencia);
    }

    @Test
    @DisplayName("Rechaza bloquear un usuario inexistente sin guardar nada")
    void usuarioInexistenteRechazado() {
        when(usuarioExistencia.existe(bloqueadoId)).thenReturn(false);

        assertThatThrownBy(() -> service.bloquear(blockerId, bloqueadoId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("USUARIO_NO_EXISTE");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getMessage()).isEqualTo("El usuario no existe");
                });

        verify(bloqueoRepository, never()).guardar(any(UUID.class), any(UUID.class));
        verify(bloqueoRepository, never()).existe(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Rechaza un bloqueo duplicado sin guardar un segundo registro")
    void bloqueoDuplicadoRechazado() {
        when(usuarioExistencia.existe(bloqueadoId)).thenReturn(true);
        when(bloqueoRepository.existe(blockerId, bloqueadoId)).thenReturn(true);

        assertThatThrownBy(() -> service.bloquear(blockerId, bloqueadoId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("BLOQUEO_DUPLICADO");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(exception.getMessage())
                            .isEqualTo("Este usuario ya se encuentra bloqueado");
                });

        verify(bloqueoRepository, never()).guardar(any(UUID.class), any(UUID.class));
    }

    @Test
    @DisplayName("Solo depende de puertos de dominio, nunca de repositorios Spring Data")
    void soloDependeDePuertos() {
        for (Field field : BloquearUsuarioService.class.getDeclaredFields()) {
            assertThat(field.getType().isInterface())
                    .as("el campo %s debe ser una interfaz", field.getName())
                    .isTrue();
            assertThat(field.getType().getName())
                    .as("el campo %s no debe ser un repositorio Spring Data", field.getName())
                    .doesNotContain("JpaRepository");
        }

        Constructor<?>[] constructores = BloquearUsuarioService.class.getConstructors();
        assertThat(constructores).hasSize(1);
        for (Class<?> tipo : constructores[0].getParameterTypes()) {
            assertThat(tipo)
                    .as("cada parámetro del constructor debe ser un puerto de dominio")
                    .isIn(UsuarioBloqueoRepositoryPort.class, UsuarioExistenciaPort.class);
        }
    }
}
