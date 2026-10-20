package com.codefactory.dev_social_network.usuarios.client;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.codefactory.dev_social_network.usuarios.interfaces.CredencialCreationPort;

@Component
public class CredencialHttpAdapter implements CredencialCreationPort {

    private final RestClient restClient;

    public CredencialHttpAdapter(@Value("${autenticacion.url}") String autenticacionUrl,
                                 @Value("${app.internal.token}") String internalToken) {
        this.restClient = RestClient.builder()
                .baseUrl(autenticacionUrl)
                .defaultHeader("X-Internal-Token", internalToken)
                .build();
    }

    @Override
    public void crearCredencialLocal(UUID usuarioId, String passwordHash) {
        restClient.post()
                .uri("/api/v1/auth/internal/credenciales")
                .body(Map.of(
                        "usuarioId", usuarioId.toString(),
                        "passwordHash", passwordHash))
                .retrieve()
                .toBodilessEntity();
    }
}