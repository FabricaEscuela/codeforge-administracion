package com.codefactory.dev_social_network.Mensajeria.interfaces;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationUnreadSummary;

import java.util.List;
import java.util.UUID;

public interface ConversationSummaryReaderPort {

    List<ConversationUnreadSummary> listByParticipant(UUID userId);
}