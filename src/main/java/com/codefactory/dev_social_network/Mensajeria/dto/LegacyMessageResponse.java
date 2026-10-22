package com.codefactory.dev_social_network.Mensajeria.dto;

import java.time.Instant;
import java.util.UUID;

public record LegacyMessageResponse(
        UUID id,
        UUID conversationId,
        UUID senderId,
        String content,
        Instant sentAt
) {
}
