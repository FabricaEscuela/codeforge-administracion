package com.codefactory.dev_social_network.comentarios.interfaces;

import java.util.Map;
import java.util.UUID;

public interface CountCommentRepliesUseCase {
    Map<UUID, Long> countByProject(UUID projectId);
}
