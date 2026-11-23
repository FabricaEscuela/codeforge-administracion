package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.UUID;

public interface UsuarioExistenciaPort {

    boolean existe(UUID usuarioId);
}
