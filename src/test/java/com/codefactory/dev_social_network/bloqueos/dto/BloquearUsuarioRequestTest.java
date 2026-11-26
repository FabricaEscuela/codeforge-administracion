package com.codefactory.dev_social_network.bloqueos.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

/**
 * Verifica que el cuerpo HTTP con un identificador no UUID se deserialice sin
 * errores de lectura, para que la traducción a "El usuario no existe" ocurra
 * en el caso de uso y no en Jackson.
 */
class BloquearUsuarioRequestTest {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Test
    @DisplayName("Acepta un usuarioId textual no UUID sin fallar la deserialización")
    void aceptaUsuarioIdTextualNoUuid() {
        BloquearUsuarioRequest request =
                mapper.readValue("{\"usuarioId\":\"9999\"}", BloquearUsuarioRequest.class);

        assertThat(request.getUsuarioId()).isEqualTo("9999");
    }

    @Test
    @DisplayName("Acepta un usuarioId numérico sin fallar la deserialización")
    void aceptaUsuarioIdNumerico() {
        BloquearUsuarioRequest request =
                mapper.readValue("{\"usuarioId\":9999}", BloquearUsuarioRequest.class);

        assertThat(request.getUsuarioId()).isEqualTo("9999");
    }

    @Test
    @DisplayName("Acepta un cuerpo sin usuarioId y lo deja en null")
    void aceptaCuerpoSinUsuarioId() {
        BloquearUsuarioRequest request = mapper.readValue("{}", BloquearUsuarioRequest.class);

        assertThat(request.getUsuarioId()).isNull();
    }

    @Test
    @DisplayName("No lanza errores de lectura con un usuarioId nulo")
    void aceptaUsuarioIdNulo() {
        assertThatCode(() -> mapper.readValue("{\"usuarioId\":null}", BloquearUsuarioRequest.class))
                .doesNotThrowAnyException();
    }
}
