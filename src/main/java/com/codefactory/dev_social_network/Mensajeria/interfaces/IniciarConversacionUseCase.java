package com.codefactory.dev_social_network.Mensajeria.interfaces;

import java.util.UUID;

import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;
import com.codefactory.dev_social_network.Mensajeria.dto.IniciarConversacionRequest;

public interface IniciarConversacionUseCase {

    InicioConversacionResultado iniciar(UUID usuarioId, IniciarConversacionRequest request);
}
