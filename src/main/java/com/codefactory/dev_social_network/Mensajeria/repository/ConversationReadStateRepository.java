package com.codefactory.dev_social_network.Mensajeria.repository;

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationReadStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversationReadStateRepository extends JpaRepository<ConversationReadStateEntity, UUID> {

    Optional<ConversationReadStateEntity> findByConversation_IdAndUserId(UUID conversationId, UUID userId);
}