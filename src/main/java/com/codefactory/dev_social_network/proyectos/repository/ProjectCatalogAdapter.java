package com.codefactory.dev_social_network.proyectos.repository;

import com.codefactory.dev_social_network.proyectos.dto.ProjectListItem;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectCatalogPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ProjectCatalogAdapter implements ProjectCatalogPort {

    private final ProjectRepository projectRepository;

    public ProjectCatalogAdapter(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectListItem> listRecent() {
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(project -> new ProjectListItem(
                        project.getId(),
                        project.getUserId(),
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
