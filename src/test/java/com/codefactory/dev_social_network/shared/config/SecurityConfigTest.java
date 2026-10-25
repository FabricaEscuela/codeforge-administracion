package com.codefactory.dev_social_network.shared.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

class SecurityConfigTest {

    @Test
    @DisplayName("responde 401 con el mensaje de iniciar sesion cuando no hay sesion activa")
    void respondeNoAutenticado() throws IOException {
        // Arrange
        SecurityConfig config = new SecurityConfig(null);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/discussions");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        config.responderNoAutenticado(request, response,
                new InsufficientAuthenticationException("sin sesion"));

        // Assert
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).startsWith("application/json");
        assertThat(response.getContentAsString(java.nio.charset.StandardCharsets.UTF_8))
                .contains("\"errorCode\":\"AUTHENTICATION_REQUIRED\"")
                .contains("\"message\":\"Debes iniciar sesion para continuar\"");
    }
}
