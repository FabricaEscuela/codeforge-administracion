package com.codefactory.dev_social_network.proyectos.dto;

import java.util.UUID;

public record ProjectLikeResponse(
        UUID projectId,
        String projectTitle,
        long likesCount,
        boolean liked
) {
}