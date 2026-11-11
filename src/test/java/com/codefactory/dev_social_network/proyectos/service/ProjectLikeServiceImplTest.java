package com.codefactory.dev_social_network.proyectos.service;

import com.codefactory.dev_social_network.proyectos.dto.ProjectLikeResponse;
import com.codefactory.dev_social_network.proyectos.dto.ProjectSummary;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectFinderPort;
import com.codefactory.dev_social_network.proyectos.interfaces.ProjectLikePort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectLikeServiceImplTest {

    private final ProjectFinderPort projectFinderPort = mock(ProjectFinderPort.class);
    private final ProjectLikePort projectLikePort = mock(ProjectLikePort.class);
    private final ProjectLikeServiceImpl service =
            new ProjectLikeServiceImpl(projectFinderPort, projectLikePort);
    private final UUID usuarioId = UUID.randomUUID();
    private final UUID projectId = UUID.randomUUID();
    private final UUID otroProyecto = UUID.randomUUID();

    @Test
    void darLikeExitosamente() {
        when(projectFinderPort.buscarResumenPorId(projectId))
                .thenReturn(Optional.of(new ProjectSummary(projectId, "DevConnect")));
        when(projectLikePort.registrarLike(projectId, usuarioId)).thenReturn(true);
        when(projectLikePort.contarLikes(projectId)).thenReturn(1L);

        ProjectLikeResponse response = service.likeProject(projectId, usuarioId);

        assertThat(response.projectTitle()).isEqualTo("DevConnect");
        assertThat(response.likesCount()).isEqualTo(1);
        assertThat(response.liked()).isTrue();
    }

    @Test
    void likeRepetidoNoCambiaNada() {
        when(projectFinderPort.buscarResumenPorId(projectId))
                .thenReturn(Optional.of(new ProjectSummary(projectId, "DevConnect")));
        when(projectLikePort.registrarLike(projectId, usuarioId)).thenReturn(false);

        assertThatThrownBy(() -> service.likeProject(projectId, usuarioId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Ya le haz dado like a este proyecto");

        verify(projectLikePort, never()).contarLikes(any(UUID.class));
    }

    @Test
    void proyectoInexistente() {
        when(projectFinderPort.buscarResumenPorId(otroProyecto)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.likeProject(otroProyecto, usuarioId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El proyecto no existe");

        verifyNoInteractions(projectLikePort);
    }
}