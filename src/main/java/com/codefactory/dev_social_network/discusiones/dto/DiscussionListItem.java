package com.codefactory.dev_social_network.discusiones.dto;

import com.codefactory.dev_social_network.discusiones.entity.DiscussionStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DiscussionListItem(
        UUID id,
        UUID userId,
        UUID technologyId,
        String title,
        String content,
        DiscussionStatus status,
        OffsetDateTime createdAt
) {
}
