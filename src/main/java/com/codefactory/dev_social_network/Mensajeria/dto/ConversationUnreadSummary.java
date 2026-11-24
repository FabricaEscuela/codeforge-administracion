package com.codefactory.dev_social_network.Mensajeria.dto;

import java.util.UUID;

public record ConversationUnreadSummary(UUID conversationId, UUID otherUserId, long unreadCount) {
}