package com.codefactory.dev_social_network.comentarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
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

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.dto.CreateReplyRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ReplyToCommentServiceTest {

    private static final UUID PROYECTO = UUID.randomUUID();
    private static final UUID COMENTARIO = UUID.randomUUID();
    private static final UUID USUARIO = UUID.randomUUID();

    @Mock
    private CommentInProjectGuard guard;
    @Mock
    private CommentReplyRepositoryPort replyRepository;
    @Mock
    private CommentAuthorDirectoryPort authorDirectory;

    private ReplyToCommentService servicio() {
        return new ReplyToCommentService(guard, replyRepository, authorDirectory);
    }

    private void dadoQueSeGuardaCon(String contenido, long totalTrasGuardar) {
        when(replyRepository.save(any(CommentReply.class))).thenReturn(
                new CommentReply(UUID.randomUUID(), COMENTARIO, USUARIO, contenido, OffsetDateTime.now()));
        when(authorDirectory.usernamesFor(Set.of(USUARIO))).thenReturn(Map.of(USUARIO, "LuisDev"));
        when(replyRepository.countByCommentId(COMENTARIO)).thenReturn(totalTrasGuardar);
    }

    @Test
    @DisplayName("Escenario 1: publica la respuesta, muestra 'Respuesta publicada', usuario, fecha y 1 respuesta")
    void publicaLaRespuesta() {
        // Arrange
        dadoQueSeGuardaCon("Estoy de acuerdo con tu propuesta", 1);

        // Act
        PublishedReplyResponse resultado = servicio().reply(USUARIO, PROYECTO, COMENTARIO,
                new CreateReplyRequest("  Estoy de acuerdo con tu propuesta  "));

        // Assert
        assertThat(resultado.mensaje()).isEqualTo("Respuesta publicada");
        assertThat(resultado.respuesta().idComentario()).isEqualTo(COMENTARIO);
        assertThat(resultado.respuesta().username()).isEqualTo("LuisDev");
        assertThat(resultado.respuesta().fechaCreacion()).isNotNull();
        assertThat(resultado.respuesta().respuesta()).isEqualTo("Estoy de acuerdo con tu propuesta");
        assertThat(resultado.totalRespuestas()).isEqualTo(1);

        ArgumentCaptor<CommentReply> guardada = ArgumentCaptor.forClass(CommentReply.class);
        verify(replyRepository).save(guardada.capture());
        assertThat(guardada.getValue().commentId()).isEqualTo(COMENTARIO);
        assertThat(guardada.getValue().userId()).isEqualTo(USUARIO);
        assertThat(guardada.getValue().content()).isEqualTo("Estoy de acuerdo con tu propuesta");
    }

    @Test
    @DisplayName("Escenario 3: un comentario con 2 respuestas pasa a tener 3 y no se toca ninguna anterior")
    void agregaAUnComentarioConRespuestas() {
        // Arrange
        dadoQueSeGuardaCon("También considero importante esta mejora", 3);

        // Act
        PublishedReplyResponse resultado = servicio().reply(USUARIO, PROYECTO, COMENTARIO,
                new CreateReplyRequest("También considero importante esta mejora"));

        // Assert
        assertThat(resultado.mensaje()).isEqualTo("Respuesta publicada");
        assertThat(resultado.totalRespuestas()).isEqualTo(3);
        verify(replyRepository).save(any(CommentReply.class));
        verify(replyRepository, never()).findByCommentId(any());
    }

    @Test
    @DisplayName("Escenario 6: publica una respuesta de exactamente 1000 caracteres")
    void publicaElMaximoPermitido() {
        // Arrange
        String maximo = "a".repeat(1000);
        dadoQueSeGuardaCon(maximo, 1);

        // Act
        PublishedReplyResponse resultado = servicio().reply(USUARIO, PROYECTO, COMENTARIO,
                new CreateReplyRequest(maximo));

        // Assert
        assertThat(resultado.mensaje()).isEqualTo("Respuesta publicada");
        assertThat(resultado.respuesta().respuesta()).hasSize(1000);
        assertThat(resultado.totalRespuestas()).isEqualTo(1);
    }

    @Test
    @DisplayName("Escenario 8: comentario inexistente -> 'El comentario no existe' y no se registra respuesta")
    void noRespondeUnComentarioInexistente() {
        // Arrange
        doThrow(new BusinessException("COMMENT_NOT_FOUND", "El comentario no existe", HttpStatus.NOT_FOUND))
                .when(guard).ensureCommentBelongsToProject(PROYECTO, COMENTARIO);

        // Act y Assert
        assertThatThrownBy(() -> servicio().reply(USUARIO, PROYECTO, COMENTARIO,
                new CreateReplyRequest("Buen aporte")))
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("El comentario no existe"));
        verify(replyRepository, never()).save(any());
        verifyNoInteractions(authorDirectory);
    }

    @Test
    @DisplayName("Escenario 9: comentario de otro proyecto -> 'El comentario no pertenece a este proyecto'")
    void noRespondeUnComentarioDeOtroProyecto() {
        // Arrange
        doThrow(new BusinessException("COMMENT_NOT_IN_PROJECT",
                "El comentario no pertenece a este proyecto", HttpStatus.NOT_FOUND))
                .when(guard).ensureCommentBelongsToProject(PROYECTO, COMENTARIO);

        // Act y Assert
        assertThatThrownBy(() -> servicio().reply(USUARIO, PROYECTO, COMENTARIO,
                new CreateReplyRequest("Buen aporte")))
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("El comentario no pertenece a este proyecto"));
        verify(replyRepository, never()).save(any());
    }
}
