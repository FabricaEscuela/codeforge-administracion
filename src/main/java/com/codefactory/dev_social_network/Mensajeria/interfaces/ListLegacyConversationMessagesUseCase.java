package com.codefactory.dev_social_network.Mensajeria.interfaces;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacyMessageResponse;

import java.util.List;
import java.util.UUID;

public interface ListLegacyConversationMessagesUseCase {
    List<LegacyMessageResponse> list(UUID requesterId, UUID conversationId);
}
