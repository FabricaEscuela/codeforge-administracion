package com.codefactory.dev_social_network.proyectos.interfaces;

import com.codefactory.dev_social_network.proyectos.dto.ProjectListItem;

import java.util.List;

public interface ProjectCatalogPort {
    List<ProjectListItem> listRecent();
}
