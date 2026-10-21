package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;
import com.codefactory.dev_social_network.proyectos.entity.Project;
import com.codefactory.dev_social_network.proyectos.entity.Technology;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectDetailFinderPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectDetailFinderAdapter implements ProjectDetailFinderPort {

    private final ProjectRepository projectRepository;

    public ProjectDetailFinderAdapter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDetail> buscarDetallePorId(Long projectId) {
        return projectRepository.findById(projectId).map(this::toDetail);
    }

    private ProjectDetail toDetail(Project project) {
        List<String> technologies = project.getTechnologies() == null
                ? List.of()
                : project.getTechnologies().stream().map(Technology::getName).toList();

        return new ProjectDetail(
                project.getId(),
                project.getUserId(),
                project.getTitle(),
                project.getDescription(),
                project.getRepositoryUrl(),
                project.getCreatedAt(),
                technologies
        );
    }
}
