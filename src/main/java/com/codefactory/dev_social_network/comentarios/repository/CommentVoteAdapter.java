package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.entity.CommentVoteEntity;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentVotePort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class CommentVoteAdapter implements CommentVotePort {

    private final CommentVoteJpaRepository repository;

    public CommentVoteAdapter(CommentVoteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean registrarVoto(UUID commentId, UUID userId) {
        if (repository.existsByCommentIdAndUserId(commentId, userId)) {
            return false;
        }
        try {
            repository.saveAndFlush(new CommentVoteEntity(commentId, userId));
            return true;
        } catch (DataIntegrityViolationException e) {
            // Dos peticiones simultáneas: la clave primaria evita el voto duplicado
            return false;
        }
    }

    @Override
    public boolean eliminarVoto(UUID commentId, UUID userId) {
        return repository.deleteVote(commentId, userId) > 0;
    }

    @Override
    public long contarVotos(UUID commentId) {
        return repository.countByCommentId(commentId);
    }

    @Override
    public Map<UUID, Long> contarVotos(Collection<UUID> commentIds) {
        Map<UUID, Long> totals = new HashMap<>();
        if (commentIds.isEmpty()) {
            return totals;
        }
        for (Object[] row : repository.countByCommentIds(commentIds)) {
            totals.put((UUID) row[0], (Long) row[1]);
        }
        return totals;
    }

    @Override
    public Set<UUID> comentariosVotadosPor(UUID userId, Collection<UUID> commentIds) {
        if (commentIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(repository.findVotedCommentIds(userId, commentIds));
    }
}