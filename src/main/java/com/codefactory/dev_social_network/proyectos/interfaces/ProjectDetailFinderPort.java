package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;

import java.util.Optional;

public interface ProjectDetailFinderPort {
    Optional<ProjectDetail> buscarDetallePorId(Long projectId);
}
