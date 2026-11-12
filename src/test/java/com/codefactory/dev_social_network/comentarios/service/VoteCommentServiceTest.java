package com.codefactory.dev_social_network.comentarios.service;

import com.codefactory.dev_social_network.comentarios.domain.ProjectComment;
import com.codefactory.dev_social_network.comentarios.dto.CommentVoteResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentVotePort;
import com.codefactory.dev_social_network.comentarios.interfaces.ProjectCommentRepositoryPort;
import com.codefactory.dev_social_network.comentarios.repository.ProyectoRefRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class VoteCommentServiceTest {

    private final ProyectoRefRepository proyectos = mock(ProyectoRefRepository.class);
    private final ProjectCommentRepositoryPort comments = mock(ProjectCommentRepositoryPort.class);
    private final CommentVotePort votes = mock(CommentVotePort.class);
    private final VoteCommentService service = new VoteCommentService(proyectos, comments, votes);

    private final UUID userId = UUID.randomUUID();
    private final UUID commentId = UUID.randomUUID();

    private final UUID proyecto = UUID.randomUUID();
    private final UUID otroProyecto = UUID.randomUUID();

    private void projectExists() {
        when(proyectos.existsById(proyecto)).thenReturn(true);
    }

    private void commentInProject(UUID projectId) {
        when(comments.findById(commentId)).thenReturn(Optional.of(
                new ProjectComment(commentId, projectId, UUID.randomUUID(), "texto", OffsetDateTime.now())));
    }

    @Test
    void votarExitosamente() {
        projectExists();
        commentInProject(proyecto);
        when(votes.registrarVoto(commentId, userId)).thenReturn(true);
        when(votes.contarVotos(commentId)).thenReturn(3L);

        CommentVoteResponse response = service.vote(userId, proyecto, commentId);

        assertThat(response.votos()).isEqualTo(3);
        assertThat(response.votadoPorMi()).isTrue();
        assertThat(response.mensaje()).isEqualTo("Voto registrado");
    }

    @Test
    void retirarVoto() {
        projectExists();
        commentInProject(proyecto);
        when(votes.eliminarVoto(commentId, userId)).thenReturn(true);
        when(votes.contarVotos(commentId)).thenReturn(2L);

        CommentVoteResponse response = service.removeVote(userId, proyecto, commentId);

        assertThat(response.votos()).isEqualTo(2);
        assertThat(response.votadoPorMi()).isFalse();
        assertThat(response.mensaje()).isEqualTo("Voto eliminado");
    }

    @Test
    void retirarVotoQueNoExisteEsIdempotente() {
        projectExists();
        commentInProject(proyecto);
        when(votes.eliminarVoto(commentId, userId)).thenReturn(false);
        when(votes.contarVotos(commentId)).thenReturn(2L);

        assertThat(service.removeVote(userId, proyecto, commentId).votos()).isEqualTo(2);
    }

    @Test
    void votarDeNuevoRetiraElVotoSinDuplicar() {
        projectExists();
        commentInProject(proyecto);
        when(votes.registrarVoto(commentId, userId)).thenReturn(false);
        when(votes.contarVotos(commentId)).thenReturn(2L);

        CommentVoteResponse response = service.vote(userId, proyecto, commentId);

        assertThat(response.votos()).isEqualTo(2);
        assertThat(response.votadoPorMi()).isFalse();
        assertThat(response.mensaje()).isEqualTo("Voto eliminado");
        verify(votes).eliminarVoto(commentId, userId);
    }

    @Test
    void comentarioInexistente() {
        projectExists();
        when(comments.findById(commentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.vote(userId, proyecto, commentId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El comentario no existe");
        verify(votes, never()).registrarVoto(any(), any());
    }

    @Test
    void comentarioDeOtroProyecto() {
        projectExists();
        commentInProject(otroProyecto);

        assertThatThrownBy(() -> service.vote(userId, proyecto, commentId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El comentario no pertenece a este proyecto");
        verify(votes, never()).registrarVoto(any(), any());
    }

    @Test
    void proyectoInexistente() {
        when(proyectos.existsById(proyecto)).thenReturn(false);

        assertThatThrownBy(() -> service.vote(userId, proyecto, commentId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El proyecto no existe");
        verify(votes, never()).registrarVoto(any(), any());
    }
}