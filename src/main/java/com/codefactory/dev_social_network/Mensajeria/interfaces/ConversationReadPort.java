package com.codefactory.dev_social_network.Mensajeria.interfaces;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ConversationReadPort {

    void markAsRead(UUID readerId, UUID otherUserId, OffsetDateTime readAt);
}