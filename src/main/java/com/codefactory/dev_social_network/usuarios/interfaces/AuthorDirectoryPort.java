package com.codefactory.dev_social_network.usuarios.interfaces;

import com.codefactory.dev_social_network.usuarios.dto.UserSummary;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface AuthorDirectoryPort {
    Map<UUID, UserSummary> findByIds(Set<UUID> userIds);
}