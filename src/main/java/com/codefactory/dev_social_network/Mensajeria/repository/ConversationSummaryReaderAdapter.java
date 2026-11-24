package com.codefactory.dev_social_network.Mensajeria.repository;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationUnreadSummary;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationSummaryReaderPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ConversationSummaryReaderAdapter implements ConversationSummaryReaderPort {

    private final ConversationQueryRepository conversationQueryRepository;

    public ConversationSummaryReaderAdapter(ConversationQueryRepository conversationQueryRepository) {
        this.conversationQueryRepository = conversationQueryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationUnreadSummary> listByParticipant(UUID userId) {
        Map<UUID, Long> unreadByConversation = conversationQueryRepository.countUnreadByConversation(userId)
                .stream()
                .collect(Collectors.toMap(
                        ConversationQueryRepository.UnreadCountView::getConversationId,
                        ConversationQueryRepository.UnreadCountView::getUnreadCount));

        return conversationQueryRepository.findAllByParticipant(userId).stream()
                .map(conversation -> new ConversationUnreadSummary(
                        conversation.getId(),
                        otherParticipant(conversation, userId),
                        unreadByConversation.getOrDefault(conversation.getId(), 0L)))
                .toList();
    }

    private static UUID otherParticipant(ConversationEntity conversation, UUID userId) {
        return conversation.getParticipantOneId().equals(userId)
                ? conversation.getParticipantTwoId()
                : conversation.getParticipantOneId();
    }
}