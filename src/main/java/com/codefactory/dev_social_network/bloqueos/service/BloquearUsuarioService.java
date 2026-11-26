package com.codefactory.dev_social_network.bloqueos.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloquearUsuarioUseCase;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

@Service
public class BloquearUsuarioService implements BloquearUsuarioUseCase {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository;
    private final UsuarioExistenciaPort usuarioExistencia;

    public BloquearUsuarioService(UsuarioBloqueoRepositoryPort bloqueoRepository,
                                  UsuarioExistenciaPort usuarioExistencia) {
        this.bloqueoRepository = bloqueoRepository;
        this.usuarioExistencia = usuarioExistencia;
    }

    @Override
    @Transactional
    public BloqueoResponse bloquear(UUID blockerId, UUID usuarioId) {
        if (usuarioId != null && usuarioId.equals(blockerId)) {
            throw new BusinessException(
                    "AUTO_BLOQUEO",
                    "No puedes bloquear tu propio usuario",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (usuarioId == null || !usuarioExistencia.existe(usuarioId)) {
            throw new BusinessException(
                    "USUARIO_NO_EXISTE",
                    "El usuario no existe",
                    HttpStatus.NOT_FOUND
            );
        }

        if (bloqueoRepository.existe(blockerId, usuarioId)) {
            throw new BusinessException(
                    "BLOQUEO_DUPLICADO",
                    "Este usuario ya se encuentra bloqueado",
                    HttpStatus.CONFLICT
            );
        }

        bloqueoRepository.guardar(blockerId, usuarioId);

        return new BloqueoResponse("Usuario bloqueado exitosamente");
    }
}
