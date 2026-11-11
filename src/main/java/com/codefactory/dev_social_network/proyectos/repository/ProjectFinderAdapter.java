package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.ProjectSummary;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectFinderPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProjectFinderAdapter implements ProjectFinderPort {

    private final ProjectRepository projectRepository;

    public ProjectFinderAdapter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<ProjectSummary> buscarResumenPorId(UUID projectId) {
        return projectRepository.findById(projectId)
                .map(project -> new ProjectSummary(project.getId(), project.getTitle()));
    }
}