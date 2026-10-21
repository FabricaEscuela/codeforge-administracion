package com.codefactory.dev_social_network.proyectos.controller;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;
import com.codefactory.dev_social_network.proyectos.dto.ProjectListItem;
import com.codefactory.dev_social_network.proyectos.interfaces.GetProjectUseCase;
import com.codefactory.dev_social_network.proyectos.interfaces.ListProjectsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectQueryController {

    private final GetProjectUseCase getProjectUseCase;
    private final ListProjectsUseCase listProjectsUseCase;

    public ProjectQueryController(GetProjectUseCase getProjectUseCase,
                                  ListProjectsUseCase listProjectsUseCase) {
        this.getProjectUseCase = getProjectUseCase;
        this.listProjectsUseCase = listProjectsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProjectListItem>> list() {
        return ResponseEntity.ok(listProjectsUseCase.listRecent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetail> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getProjectUseCase.getById(id));
    }
}
