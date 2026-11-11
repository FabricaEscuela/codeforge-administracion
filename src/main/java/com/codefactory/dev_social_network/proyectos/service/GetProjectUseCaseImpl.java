package com.codefactory.dev_social_network.proyectos.service;

import com.codefactory.dev_social_network.proyectos.dto.ProjectDetail;
import com.codefactory.dev_social_network.proyectos.interfaces.GetProjectUseCase;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectDetailFinderPort;
import com.codefactory.dev_social_network.shared.exception.ProyectoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProjectUseCaseImpl implements GetProjectUseCase {

    private final ProjectDetailFinderPort projectDetailFinderPort;

    public GetProjectUseCaseImpl(ProjectDetailFinderPort projectDetailFinderPort) {
        this.projectDetailFinderPort = projectDetailFinderPort;
    }

    @Override
    public ProjectDetail getById(UUID projectId) {
        return projectDetailFinderPort.buscarDetallePorId(projectId)
                .orElseThrow(ProyectoNoEncontradoException::new);
    }
}