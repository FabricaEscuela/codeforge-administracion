package com.codefactory.dev_social_network.comentarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.codefactory.dev_social_network.comentarios.interfaces.CommentReplyRepositoryPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class CountCommentRepliesServiceTest {

    private static final UUID PROYECTO = UUID.randomUUID();

    @Mock
    private CommentInProjectGuard guard;
    @Mock
    private CommentReplyRepositoryPort replyRepository;

    private CountCommentRepliesService servicio() {
        return new CountCommentRepliesService(guard, replyRepository);
    }

    @Test
    @DisplayName("Escenario 2: devuelve el total de respuestas de cada comentario del proyecto")
    void devuelveLosTotalesPorComentario() {
        // Arrange
        UUID c501 = UUID.randomUUID();
        UUID c502 = UUID.randomUUID();
        when(replyRepository.countByProjectId(PROYECTO)).thenReturn(Map.of(c501, 2L, c502, 5L));

        // Act
        Map<UUID, Long> totales = servicio().countByProject(PROYECTO);

        // Assert
        assertThat(totales).containsEntry(c501, 2L).containsEntry(c502, 5L).hasSize(2);
    }

    @Test
    @DisplayName("falla con 'El proyecto no existe' si el proyecto no existe")
    void rechazaProyectoInexistente() {
        // Arrange
        doThrow(new BusinessException("PROJECT_NOT_FOUND", "El proyecto no existe", HttpStatus.NOT_FOUND))
                .when(guard).ensureProjectExists(PROYECTO);

        // Act y Assert
        assertThatThrownBy(() -> servicio().countByProject(PROYECTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El proyecto no existe");
        verifyNoInteractions(replyRepository);
    }
}