package com.codefactory.dev_social_network.discusiones.dto;

import com.codefactory.dev_social_network.discusiones.entity.DiscussionStatus;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DiscussionDetailResponse(
        Long id,
        String title,
        DiscussionStatus status,
        UUID userId,
        UserSummary author,
        String content,
        Long technologyId,
        OffsetDateTime createdAt
) {
}