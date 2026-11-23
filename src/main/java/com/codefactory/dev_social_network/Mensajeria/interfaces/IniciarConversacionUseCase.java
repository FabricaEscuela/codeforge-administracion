package com.codefactory.dev_social_network.Mensajeria.interfaces;

import java.util.UUID;

import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;

public interface IniciarConversacionUseCase {

    InicioConversacionResultado iniciar(UUID usuarioId, UUID otroUsuarioId);
}
