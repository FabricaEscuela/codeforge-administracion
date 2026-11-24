package com.codefactory.dev_social_network.comentarios.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.codefactory.dev_social_network.comentarios.dto.CommentRepliesResponse;
import com.codefactory.dev_social_network.comentarios.dto.CreateReplyRequest;
import com.codefactory.dev_social_network.comentarios.dto.PublishedReplyResponse;
import com.codefactory.dev_social_network.comentarios.dto.ReplyResponse;
import com.codefactory.dev_social_network.comentarios.interfaces.CountCommentRepliesUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ListCommentRepliesUseCase;
import com.codefactory.dev_social_network.comentarios.interfaces.ReplyToCommentUseCase;

import jakarta.validation.Valid;

@ExtendWith(MockitoExtension.class)
class CommentReplyControllerTest {

    private static final UUID PROYECTO = UUID.randomUUID();
    private static final UUID COMENTARIO = UUID.randomUUID();
    private static final UUID USUARIO = UUID.randomUUID();

    @Mock
    private ReplyToCommentUseCase replyUseCase;
    @Mock
    private ListCommentRepliesUseCase listUseCase;
    @Mock
    private CountCommentRepliesUseCase countUseCase;

    @InjectMocks
    private CommentReplyController controller;

    private static ReplyResponse unaRespuesta() {
        return new ReplyResponse(UUID.randomUUID(), COMENTARIO, USUARIO, "LuisDev", "Hola", OffsetDateTime.now());
    }

    @Test
    @DisplayName("POST responde 201 con 'Respuesta publicada' y delega con el usuario autenticado")
    void respondeConCreated() {
        // Arrange
        CreateReplyRequest peticion = new CreateReplyRequest("Hola");
        PublishedReplyResponse publicada = new PublishedReplyResponse("Respuesta publicada", unaRespuesta(), 1);
        when(replyUseCase.reply(USUARIO, PROYECTO, COMENTARIO, peticion)).thenReturn(publicada);

        // Act
        ResponseEntity<PublishedReplyResponse> respuesta = controller.reply(USUARIO, PROYECTO, COMENTARIO, peticion);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isSameAs(publicada);
        verify(replyUseCase).reply(USUARIO, PROYECTO, COMENTARIO, peticion);
    }

    @Test
    @DisplayName("GET de respuestas responde 200 con el hilo del comentario")
    void listaElHilo() {
        // Arrange
        CommentRepliesResponse hilo = new CommentRepliesResponse(COMENTARIO, 1, List.of(unaRespuesta()));
        when(listUseCase.list(PROYECTO, COMENTARIO)).thenReturn(hilo);

        // Act
        ResponseEntity<CommentRepliesResponse> respuesta = controller.list(PROYECTO, COMENTARIO);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).isSameAs(hilo);
    }

    @Test
    @DisplayName("GET reply-counts responde 200 con los totales por comentario")
    void devuelveLosTotales() {
        // Arrange
        Map<UUID, Long> totales = Map.of(COMENTARIO, 2L);
        when(countUseCase.countByProject(PROYECTO)).thenReturn(totales);

        // Act
        ResponseEntity<Map<UUID, Long>> respuesta = controller.counts(PROYECTO);

        // Assert
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).containsEntry(COMENTARIO, 2L);
    }

    @Test
    @DisplayName("Escenarios 4 y 5: el cuerpo del POST se valida con @Valid antes de llegar al caso de uso")
    void elCuerpoSeValida() throws NoSuchMethodException {
        // Arrange
        Method metodo = CommentReplyController.class.getMethod("reply",
                UUID.class, UUID.class, UUID.class, CreateReplyRequest.class);

        // Act
        boolean validado = metodo.getParameters()[3].isAnnotationPresent(Valid.class);

        // Assert
        assertThat(validado).isTrue();
    }
}