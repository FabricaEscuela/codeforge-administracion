package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;
import com.codefactory.dev_social_network.proyectos.entity.Project;
import com.codefactory.dev_social_network.proyectos.entity.Technology;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectDetailFinderPort;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.AuthorDirectoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class ProjectDetailFinderAdapter implements ProjectDetailFinderPort {

    private final ProjectRepository projectRepository;
    private final AuthorDirectoryPort authorDirectoryPort;

    public ProjectDetailFinderAdapter(
            ProjectRepository projectRepository,
            AuthorDirectoryPort authorDirectoryPort
    ) {
        this.projectRepository = projectRepository;
        this.authorDirectoryPort = authorDirectoryPort;
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
        UserSummary author = null;
        if (project.getUserId() != null) {
            Map<java.util.UUID, UserSummary> authors = authorDirectoryPort.findByIds(Set.of(project.getUserId()));
            author = authors.get(project.getUserId());
        }

        return new ProjectDetail(
                project.getId(),
                project.getUserId(),
                author,
                project.getTitle(),
                project.getDescription(),
                project.getRepositoryUrl(),
                project.getCreatedAt(),
                technologies
        );
    }
}