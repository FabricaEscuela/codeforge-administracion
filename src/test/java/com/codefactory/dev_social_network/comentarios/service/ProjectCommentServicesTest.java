package com.codefactory.dev_social_network.comentarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.dto.CommentResponse;
import com.codefactory.dev_social_network.comentarios.dto.CreateCommentRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedCommentResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ProjectCommentServicesTest {

    private static final UUID PROYECTO = UUID.randomUUID();
    private static final UUID USUARIO = UUID.randomUUID();

    @Mock
    private ProyectoRefRepository proyectoRefRepository;
    @Mock
    private ProjectCommentRepositoryPort commentRepository;
    @Mock
    private CommentAuthorDirectoryPort authorDirectory;

    private CreateProjectCommentService crear() {
        return new CreateProjectCommentService(proyectoRefRepository, commentRepository, authorDirectory);
    }

    private ListProjectCommentsService listar() {
        return new ListProjectCommentsService(proyectoRefRepository, commentRepository, authorDirectory);
    }

    // ---------------------------------------------------------------- crear

    @Test
    @DisplayName("publica el comentario, responde 'Comentario publicado' con el username del autor")
    void publicaElComentario() {
        // Arrange
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(true);
        OffsetDateTime fecha = OffsetDateTime.now();
        UUID idComentario = UUID.randomUUID();
        when(commentRepository.save(any(ProjectComment.class))).thenReturn(
                new ProjectComment(idComentario, PROYECTO, USUARIO, "Buen uso de la arquitectura hexagonal", fecha));
        when(authorDirectory.usernamesFor(Set.of(USUARIO))).thenReturn(Map.of(USUARIO, "LuisDev"));

        // Act
        PublishedCommentResponse respuesta = crear().create(USUARIO, PROYECTO,
                new CreateCommentRequest("  Buen uso de la arquitectura hexagonal  "));

        // Assert
        assertThat(respuesta.mensaje()).isEqualTo("Comentario publicado");
        assertThat(respuesta.comentario().username()).isEqualTo("LuisDev");
        assertThat(respuesta.comentario().comentario()).isEqualTo("Buen uso de la arquitectura hexagonal");
        assertThat(respuesta.comentario().fechaCreacion()).isEqualTo(fecha);

        ArgumentCaptor<ProjectComment> guardado = ArgumentCaptor.forClass(ProjectComment.class);
        verify(commentRepository).save(guardado.capture());
        assertThat(guardado.getValue().projectId()).isEqualTo(PROYECTO);
        assertThat(guardado.getValue().userId()).isEqualTo(USUARIO);
        assertThat(guardado.getValue().content()).isEqualTo("Buen uso de la arquitectura hexagonal");
    }

    @Test
    @DisplayName("falla con 404 'El proyecto no existe' y no guarda nada si el proyecto no existe")
    void noComentaUnProyectoInexistente() {
        // Arrange
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(false);

        // Act y Assert
        assertThatThrownBy(() -> crear().create(USUARIO, PROYECTO, new CreateCommentRequest("Hola")))
                .isInstanceOfSatisfying(BusinessException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("El proyecto no existe");
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                });
        verify(commentRepository, never()).save(any());
    }

    // --------------------------------------------------------------- listar

    @Test
    @DisplayName("lista los comentarios del proyecto con el username de cada autor")
    void listaLosComentarios() {
        // Arrange
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(true);
        OffsetDateTime fecha = OffsetDateTime.now();
        ProjectComment comentario = new ProjectComment(UUID.randomUUID(), PROYECTO, USUARIO, "Hola", fecha);
        when(commentRepository.findByProjectId(PROYECTO)).thenReturn(List.of(comentario));
        when(authorDirectory.usernamesFor(Set.of(USUARIO))).thenReturn(Map.of(USUARIO, "LuisDev"));

        // Act
        List<CommentResponse> respuesta = listar().list(PROYECTO);

        // Assert
        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).username()).isEqualTo("LuisDev");
        assertThat(respuesta.get(0).comentario()).isEqualTo("Hola");
    }

    @Test
    @DisplayName("al listar un proyecto inexistente falla con 'El proyecto no existe'")
    void noListaUnProyectoInexistente() {
        // Arrange
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(false);

        // Act y Assert
        assertThatThrownBy(() -> listar().list(PROYECTO))
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("El proyecto no existe"));
    }
}
