package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.ProjectListItem;
import com.codefactory.dev_social_network.proyectos.entity.Project;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectCatalogPort;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.AuthorDirectoryPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProjectCatalogAdapter implements ProjectCatalogPort {

    private final ProjectRepository projectRepository;
    private final AuthorDirectoryPort authorDirectoryPort;

    public ProjectCatalogAdapter(
            ProjectRepository projectRepository,
            AuthorDirectoryPort authorDirectoryPort
    ) {
        this.projectRepository = projectRepository;
        this.authorDirectoryPort = authorDirectoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectListItem> listRecent() {
        List<Project> projects = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Set<UUID> authorIds = projects.stream()
                .map(Project::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<UUID, UserSummary> authors = authorDirectoryPort.findByIds(authorIds);

        return projects.stream()
                .map(project -> new ProjectListItem(
                        project.getId(),
                        project.getUserId(),
                        project.getUserId() == null ? null : authors.get(project.getUserId()),
                        project.getTitle(),
                        project.getDescription(),
                        project.getRepositoryUrl(),
                        project.getCreatedAt(),
                        project.getTechnologies() == null
                                ? List.of()
                                : project.getTechnologies().stream()
                                        .map(technology -> technology.getName())
                                        .toList()
                ))
                .toList();
    }
}