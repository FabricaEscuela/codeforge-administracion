package com.codefactory.dev_social_network.Mensajeria.interfaces;

import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;

import java.util.List;
import java.util.UUID;

public interface ConversationReaderPort {
    List<MessageConversationItem> listBetween(UUID firstUserId, UUID secondUserId);
}
