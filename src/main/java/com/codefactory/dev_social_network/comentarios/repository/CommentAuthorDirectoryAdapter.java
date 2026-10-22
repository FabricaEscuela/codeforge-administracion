package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.usuarios.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class CommentAuthorDirectoryAdapter implements CommentAuthorDirectoryPort {

    private final UsuarioJpaRepository usuarioRepository;

    public CommentAuthorDirectoryAdapter(UsuarioJpaRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Map<UUID, String> usernamesFor(Set<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        Map<UUID, String> usernames = new HashMap<>();
        usuarioRepository.findAllById(userIds).forEach(user -> {
            if (user.getUsername() != null) {
                usernames.put(user.getId(), user.getUsername());
            }
        });
        return usernames;
    }
}
