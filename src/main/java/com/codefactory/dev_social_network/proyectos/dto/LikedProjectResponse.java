package com.codefactory.dev_social_network.proyectos.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LikedProjectResponse(
        UUID projectId,
        String title,
        String repositoryUrl,
        OffsetDateTime likedAt
) {
}