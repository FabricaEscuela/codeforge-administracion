package com.codefactory.dev_social_network.usuarios.repository;

import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.entity.Usuario;
import com.codefactory.dev_social_network.usuarios.interfaces.AuthorDirectoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class UsuarioAuthorDirectoryAdapter implements AuthorDirectoryPort {

    private final UsuarioJpaRepository usuarioRepository;

    public UsuarioAuthorDirectoryAdapter(UsuarioJpaRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Map<UUID, UserSummary> findByIds(Set<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        Map<UUID, UserSummary> authors = new HashMap<>();
        for (Usuario usuario : usuarioRepository.findAllById(userIds)) {
            authors.put(
                    usuario.getId(),
                    new UserSummary(
                            usuario.getId(),
                            usuario.getUsername(),
                            usuario.getNombre(),
                            usuario.getApellido(),
                            usuario.getEmail()
                    )
            );
        }
        return Map.copyOf(authors);
    }
}