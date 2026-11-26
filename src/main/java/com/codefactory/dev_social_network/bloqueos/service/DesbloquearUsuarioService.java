package com.codefactory.dev_social_network.bloqueos.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;
import com.codefactory.dev_social_network.bloqueos.interfaces.DesbloquearUsuarioUseCase;
import com.codefactory.dev_social_network.bloqueos.interfaces.UsuarioBloqueoRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@Service
public class DesbloquearUsuarioService implements DesbloquearUsuarioUseCase {

    private final UsuarioBloqueoRepositoryPort bloqueoRepository;

    public DesbloquearUsuarioService(UsuarioBloqueoRepositoryPort bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @Override
    @Transactional
    public BloqueoResponse desbloquear(UUID blockerId, UUID usuarioId) {
        boolean eliminado = bloqueoRepository.eliminar(blockerId, usuarioId);

        if (!eliminado) {
            throw new BusinessException(
                    "BLOQUEO_NO_ENCONTRADO",
                    "No existe un bloqueo para este usuario",
                    HttpStatus.NOT_FOUND
            );
        }

        return new BloqueoResponse("Usuario desbloqueado exitosamente");
    }
}
