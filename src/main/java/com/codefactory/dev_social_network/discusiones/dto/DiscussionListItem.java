package com.codefactory.dev_social_network.discusiones.dto;

import com.codefactory.dev_social_network.discusiones.entity.DiscussionStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DiscussionListItem(
        Long id,
        UUID userId,
        Long technologyId,
        String title,
        String content,
        DiscussionStatus status,
        OffsetDateTime createdAt
) {
}
