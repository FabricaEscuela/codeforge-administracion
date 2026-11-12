package com.codefactory.dev_social_network.comentarios.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CommentVotePort {

    /** @return true si se registró; false si el usuario ya había votado. */
    boolean registrarVoto(UUID commentId, UUID userId);

    /** @return true si existía el voto y se eliminó. */
    boolean eliminarVoto(UUID commentId, UUID userId);

    long contarVotos(UUID commentId);

    Map<UUID, Long> contarVotos(Collection<UUID> commentIds);

    Set<UUID> comentariosVotadosPor(UUID userId, Collection<UUID> commentIds);
}