package com.codefactory.dev_social_network.proyectos.service;

import com.codefactory.dev_social_network.proyectos.dto.ProjectListItem;
import com.codefactory.dev_social_network.proyectos.interfaces.ListProjectsUseCase;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectCatalogPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProjectsUseCaseImpl implements ListProjectsUseCase {

    private final ProjectCatalogPort projectCatalogPort;

    public ListProjectsUseCaseImpl(ProjectCatalogPort projectCatalogPort) {
        this.projectCatalogPort = projectCatalogPort;
    }

    @Override
    public List<ProjectListItem> listRecent() {
        return projectCatalogPort.listRecent();
    }
}
