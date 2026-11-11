package com.codefactory.dev_social_network.comentarios.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProjectComment(
        UUID id,
        UUID projectId,
        UUID userId,
        String content,
        OffsetDateTime createdAt
) {
}
