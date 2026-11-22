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

class CreateReplyRequestTest {

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

    private Set<String> errores(String respuesta) {
        return validator.validate(new CreateReplyRequest(respuesta)).stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    @Test
    @DisplayName("acepta una respuesta normal")
    void aceptaRespuestaNormal() {
        assertThat(errores("Estoy de acuerdo con tu propuesta")).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    @DisplayName("rechaza una respuesta nula, vacia o en blanco con 'Complete este campo'")
    void rechazaRespuestaVacia(String respuesta) {
        assertThat(errores(respuesta)).containsExactly("respuesta: Complete este campo");
    }

    @Test
    @DisplayName("acepta una respuesta de exactamente 1000 caracteres")
    void aceptaElMaximo() {
        assertThat(errores("a".repeat(1000))).isEmpty();
    }

    @Test
    @DisplayName("rechaza una respuesta de 1001 caracteres")
    void rechazaMasDelMaximo() {
        assertThat(errores("a".repeat(1001)))
                .containsExactly("respuesta: La respuesta no puede superar 1000 caracteres");
    }
}
