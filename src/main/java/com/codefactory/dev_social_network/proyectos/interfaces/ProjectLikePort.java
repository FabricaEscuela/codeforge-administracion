package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.LikedProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectLikePort {

    boolean registrarLike(UUID projectId, UUID usuarioId);

    boolean eliminarLike(UUID projectId, UUID usuarioId);

    long contarLikes(UUID projectId);

    List<LikedProjectResponse> listarPorUsuario(UUID usuarioId);
}