package com.codefactory.dev_social_network.discusiones.dto;

import com.codefactory.dev_social_network.discusiones.entity.DiscussionStatus;

import java.util.UUID;
import java.time.OffsetDateTime;

public record DiscussionDetailResponse(
        Long id,
        String title,
        DiscussionStatus status,
        UUID userId,
        String content,
        Long technologyId,
        OffsetDateTime createdAt
) {
}
