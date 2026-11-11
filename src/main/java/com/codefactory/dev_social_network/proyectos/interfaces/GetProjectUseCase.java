package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;

import java.util.UUID;

public interface GetProjectUseCase {
    ProjectDetail getById(UUID projectId);
}