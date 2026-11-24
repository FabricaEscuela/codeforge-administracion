package com.codefactory.dev_social_network.comentarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.comentarios.interfaces.CommentLookupPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class CommentInProjectGuardTest {

    private static final UUID PROYECTO = UUID.randomUUID();
    private static final UUID OTRO_PROYECTO = UUID.randomUUID();
    private static final UUID COMENTARIO = UUID.randomUUID();

    @Mock
    private ProyectoRefRepository proyectoRefRepository;
    @Mock
    private CommentLookupPort commentLookup;

    private CommentInProjectGuard guard() {
        return new CommentInProjectGuard(proyectoRefRepository, commentLookup);
    }

    @Test
    @DisplayName("deja pasar un comentario que pertenece al proyecto")
    void permiteUnComentarioDelProyecto() {
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(true);
        when(commentLookup.findProjectIdByCommentId(COMENTARIO)).thenReturn(Optional.of(PROYECTO));

        assertThatCode(() -> guard().ensureCommentBelongsToProject(PROYECTO, COMENTARIO))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("falla con 404 'El proyecto no existe' y ni siquiera busca el comentario")
    void rechazaProyectoInexistente() {
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(false);

        assertThatThrownBy(() -> guard().ensureCommentBelongsToProject(PROYECTO, COMENTARIO))
                .isInstanceOfSatisfying(BusinessException.class, e -> {
                    assertThat(e.getErrorCode()).isEqualTo("PROJECT_NOT_FOUND");
                    assertThat(e.getMessage()).isEqualTo("El proyecto no existe");
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                });
        verifyNoInteractions(commentLookup);
    }

    @Test
    @DisplayName("falla con 404 'El comentario no existe' si el comentario no existe")
    void rechazaComentarioInexistente() {
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(true);
        when(commentLookup.findProjectIdByCommentId(COMENTARIO)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guard().ensureCommentBelongsToProject(PROYECTO, COMENTARIO))
                .isInstanceOfSatisfying(BusinessException.class, e -> {
                    assertThat(e.getErrorCode()).isEqualTo("COMMENT_NOT_FOUND");
                    assertThat(e.getMessage()).isEqualTo("El comentario no existe");
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    @DisplayName("falla con 'El comentario no pertenece a este proyecto' si es de otro proyecto")
    void rechazaComentarioDeOtroProyecto() {
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(true);
        when(commentLookup.findProjectIdByCommentId(COMENTARIO)).thenReturn(Optional.of(OTRO_PROYECTO));

        assertThatThrownBy(() -> guard().ensureCommentBelongsToProject(PROYECTO, COMENTARIO))
                .isInstanceOfSatisfying(BusinessException.class, e -> {
                    assertThat(e.getErrorCode()).isEqualTo("COMMENT_NOT_IN_PROJECT");
                    assertThat(e.getMessage()).isEqualTo("El comentario no pertenece a este proyecto");
                    assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    @DisplayName("ensureProjectExists falla con 404 si el proyecto no existe")
    void soloProyectoInexistente() {
        when(proyectoRefRepository.existsById(PROYECTO)).thenReturn(false);

        assertThatThrownBy(() -> guard().ensureProjectExists(PROYECTO))
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("El proyecto no existe"));
    }
}

