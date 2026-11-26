package com.codefactory.dev_social_network.bloqueos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

class DesbloquearUsuarioServiceTest {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository =
            mock(UsuarioBloqueoRepositoryPort.class);
    private final DesbloquearUsuarioService service =
            new DesbloquearUsuarioService(bloqueoRepository);

    private final UUID blockerId = UUID.randomUUID();
    private final UUID bloqueadoId = UUID.randomUUID();

    @Test
    @DisplayName("Desbloquea un usuario existente y devuelve el mensaje exacto")
    void desbloqueoExitoso() {
        when(bloqueoRepository.eliminar(blockerId, bloqueadoId)).thenReturn(true);

        BloqueoResponse response = service.desbloquear(blockerId, bloqueadoId);

        assertThat(response.getMensaje()).isEqualTo("Usuario desbloqueado exitosamente");
    }

    @Test
    @DisplayName("Rechaza desbloquear cuando no existe el bloqueo")
    void bloqueoInexistenteRechazado() {
        when(bloqueoRepository.eliminar(blockerId, bloqueadoId)).thenReturn(false);

        assertThatThrownBy(() -> service.desbloquear(blockerId, bloqueadoId))
                .isInstanceOfSatisfying(BusinessException.class, exception -> {
                    assertThat(exception.getErrorCode()).isEqualTo("BLOQUEO_NO_ENCONTRADO");
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getMessage())
                            .isEqualTo("No existe un bloqueo para este usuario");
                });
    }
}
