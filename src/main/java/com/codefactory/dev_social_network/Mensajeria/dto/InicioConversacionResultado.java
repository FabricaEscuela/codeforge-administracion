package com.codefactory.dev_social_network.Mensajeria.dto;

/** Resultado de iniciar una conversación: indica si la fila se creó en esta llamada. */
public record InicioConversacionResultado(ConversacionResponse conversacion, boolean creada) {
}
