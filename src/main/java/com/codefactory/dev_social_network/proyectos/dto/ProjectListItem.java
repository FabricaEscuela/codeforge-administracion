package com.codefactory.dev_social_network.proyectos.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectListItem(
        Long id,
        UUID userId,
        String title,
        String description,
        String repositoryUrl,
        OffsetDateTime createdAt,
        List<String> technologies
) {
}
