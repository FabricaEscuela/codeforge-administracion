package com.codefactory.dev_social_network.Mensajeria.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MessageConversationItem(
        UUID id,
        UUID remitenteId,
        UUID destinatarioId,
        String contenido,
        OffsetDateTime fechaEnvio
) {
}