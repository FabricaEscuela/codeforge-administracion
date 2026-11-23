package com.codefactory.dev_social_network.bloqueos.interfaces;

import java.util.UUID;

import com.codefactory.dev_social_network.bloqueos.dto.BloqueoResponse;

public interface DesbloquearUsuarioUseCase {

    BloqueoResponse desbloquear(UUID blockerId, UUID usuarioId);
}
