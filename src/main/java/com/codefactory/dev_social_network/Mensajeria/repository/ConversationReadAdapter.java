package com.codefactory.dev_social_network.Mensajeria.repository;

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationReadStateEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationReadPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class ConversationReadAdapter implements ConversationReadPort {

    private final ConversationQueryRepository conversationQueryRepository;
    private final ConversationReadStateRepository readStateRepository;

    public ConversationReadAdapter(ConversationQueryRepository conversationQueryRepository,
                                   ConversationReadStateRepository readStateRepository) {
        this.conversationQueryRepository = conversationQueryRepository;
        this.readStateRepository = readStateRepository;
    }

    @Override
    @Transactional
    public void markAsRead(UUID readerId, UUID otherUserId, OffsetDateTime readAt) {
        conversationQueryRepository.findBetween(readerId, otherUserId).ifPresent(conversation -> {
            ConversationReadStateEntity state = readStateRepository
                    .findByConversation_IdAndUserId(conversation.getId(), readerId)
                    .orElseGet(() -> new ConversationReadStateEntity(conversation, readerId, readAt));
            state.advanceTo(readAt);
            readStateRepository.save(state);
        });
    }
}