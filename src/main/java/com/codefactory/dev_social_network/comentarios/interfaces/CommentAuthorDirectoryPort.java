package com.codefactory.dev_social_network.comentarios.interfaces;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CommentAuthorDirectoryPort {
    Map<UUID, String> usernamesFor(Set<UUID> userIds);
}
