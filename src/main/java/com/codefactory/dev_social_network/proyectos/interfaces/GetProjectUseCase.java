package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;

public interface GetProjectUseCase {
    ProjectDetail getById(Long projectId);
}
