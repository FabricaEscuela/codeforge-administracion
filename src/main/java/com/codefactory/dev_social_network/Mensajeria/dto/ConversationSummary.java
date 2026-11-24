package com.codefactory.dev_social_network.Mensajeria.dto;

import java.util.UUID;

public record ConversationSummary(
        UUID conversationId,
        UUID otherUserId,
        String otherUsername,
        String otherFullName,
        long unreadCount
) {
}