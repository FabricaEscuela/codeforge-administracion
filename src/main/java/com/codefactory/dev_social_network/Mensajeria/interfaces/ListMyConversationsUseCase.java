package com.codefactory.dev_social_network.Mensajeria.interfaces;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationSummary;

import java.util.List;
import java.util.UUID;

public interface ListMyConversationsUseCase {
    List<ConversationSummary> listar(UUID usuarioId);
}