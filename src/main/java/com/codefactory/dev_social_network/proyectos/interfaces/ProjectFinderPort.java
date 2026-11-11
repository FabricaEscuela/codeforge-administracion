package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.ProjectSummary;

import java.util.Optional;
import java.util.UUID;

public interface ProjectFinderPort {

    Optional<ProjectSummary> buscarResumenPorId(UUID projectId);
}