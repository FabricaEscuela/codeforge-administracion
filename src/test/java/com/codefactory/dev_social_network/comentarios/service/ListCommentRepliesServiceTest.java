package com.codefactory.dev_social_network.comentarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.comentarios.domain.CommentReply;
import com.codefactory.dev_social_network.comentarios.dto.CommentRepliesResponse;
import com.codefactory.dev_social_network.comentarios.dto.ReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentAuthorDirectoryPort;
import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ListCommentRepliesServiceTest {

    private static final UUID PROYECTO = UUID.randomUUID();
    private static final UUID COMENTARIO = UUID.randomUUID();
    private static final UUID LUIS = UUID.randomUUID();
    private static final UUID MARIA = UUID.randomUUID();

    @Mock
    private CommentInProjectGuard guard;
    @Mock
    private CommentReplyRepositoryPort replyRepository;
    @Mock
    private CommentAuthorDirectoryPort authorDirectory;

    private ListCommentRepliesService servicio() {
        return new ListCommentRepliesService(guard, replyRepository, authorDirectory);
    }

    @Test
    @DisplayName("Escenario 2: devuelve las 2 respuestas del comentario en orden, con autor y fecha")
    void listaLasRespuestasDelComentario() {
        // Arrange
        OffsetDateTime t1 = OffsetDateTime.now().minusMinutes(5);
        OffsetDateTime t2 = OffsetDateTime.now();
        when(replyRepository.findByCommentId(COMENTARIO)).thenReturn(List.of(
                new CommentReply(UUID.randomUUID(), COMENTARIO, LUIS, "Primera", t1),
                new CommentReply(UUID.randomUUID(), COMENTARIO, MARIA, "Segunda", t2)));
        when(authorDirectory.usernamesFor(Set.of(LUIS, MARIA)))
                .thenReturn(Map.of(LUIS, "LuisDev", MARIA, "MariaDev"));

        // Act
        CommentRepliesResponse resultado = servicio().list(PROYECTO, COMENTARIO);

        // Assert
        assertThat(resultado.idComentario()).isEqualTo(COMENTARIO);
        assertThat(resultado.totalRespuestas()).isEqualTo(2);
        assertThat(resultado.respuestas()).extracting(ReplyResponse::respuesta).containsExactly("Primera", "Segunda");
        assertThat(resultado.respuestas()).extracting(ReplyResponse::username).containsExactly("LuisDev", "MariaDev");
        assertThat(resultado.respuestas()).extracting(ReplyResponse::fechaCreacion).containsExactly(t1, t2);
    }

    @Test
    @DisplayName("un comentario sin respuestas devuelve total 0 y lista vacia")
    void comentarioSinRespuestas() {
        // Arrange
        when(replyRepository.findByCommentId(COMENTARIO)).thenReturn(List.of());
        when(authorDirectory.usernamesFor(Set.of())).thenReturn(Map.of());

        // Act
        CommentRepliesResponse resultado = servicio().list(PROYECTO, COMENTARIO);

        // Assert
        assertThat(resultado.totalRespuestas()).isZero();
        assertThat(resultado.respuestas()).isEmpty();
    }

    @Test
    @DisplayName("si el comentario no pertenece al proyecto falla y no consulta respuestas")
    void propagaElErrorDelGuard() {
        // Arrange
        doThrow(new BusinessException("COMMENT_NOT_IN_PROJECT",
                "El comentario no pertenece a este proyecto", HttpStatus.NOT_FOUND))
                .when(guard).ensureCommentBelongsToProject(PROYECTO, COMENTARIO);

        // Act y Assert
        assertThatThrownBy(() -> servicio().list(PROYECTO, COMENTARIO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El comentario no pertenece a este proyecto");
        verifyNoInteractions(replyRepository);
    }
}
