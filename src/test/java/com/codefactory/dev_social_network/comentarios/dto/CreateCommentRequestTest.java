package com.codefactory.dev_social_network.comentarios.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CreateCommentRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private Set<String> errores(String comentario) {
        return validator.validate(new CreateCommentRequest(comentario)).stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("acepta un comentario normal")
    void aceptaComentarioNormal() {
        assertThat(errores("Buen uso de la arquitectura hexagonal")).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    @DisplayName("rechaza un comentario vacio con el mensaje del Gherkin")
    void rechazaComentarioVacio(String comentario) {
        assertThat(errores(comentario))
                .containsExactly("comentario: El comentario no puede estar vacio");
    }

    @Test
    @DisplayName("rechaza un comentario de 1001 caracteres con el mensaje del Gherkin")
    void rechazaComentarioDe1001Caracteres() {
        assertThat(errores("a".repeat(1001)))
                .containsExactly("comentario: Comentario excede el limite de caracteres");
    }

    @Test
    @DisplayName("acepta un comentario de exactamente 1000 caracteres")
    void aceptaComentarioDe1000Caracteres() {
        assertThat(errores("a".repeat(1000))).isEmpty();
    }
}
