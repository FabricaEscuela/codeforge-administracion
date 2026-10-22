package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacyMessageResponse;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListLegacyConversationMessagesUseCase;
import com.codefactory.dev_social_network.Mensajeria.repository.ConversationRepository;
import com.codefactory.dev_social_network.Mensajeria.repository.MessageRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class ListLegacyConversationMessagesService implements ListLegacyConversationMessagesUseCase {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ListLegacyConversationMessagesService(ConversationRepository conversationRepository,
                                                MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegacyMessageResponse> list(UUID requesterId, UUID conversationId) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new BusinessException(
                        "CONVERSATION_NOT_FOUND", "La conversación no existe", HttpStatus.NOT_FOUND));

        boolean isParticipant = requesterId.equals(conversation.getParticipantOneId())
                || requesterId.equals(conversation.getParticipantTwoId());
        if (!isParticipant) {
            throw new BusinessException(
                    "CONVERSATION_ACCESS_DENIED", "No perteneces a esta conversación", HttpStatus.FORBIDDEN);
        }

        return messageRepository.findConversation(
                        conversation.getParticipantOneId(), conversation.getParticipantTwoId())
                .stream()
                .map(message -> toResponse(message, conversationId))
                .toList();
    }

    private static LegacyMessageResponse toResponse(MessageEntity message, UUID conversationId) {
        return new LegacyMessageResponse(message.getId(), conversationId, message.getRemitenteId(),
                message.getContenido(), message.getFechaEnvio().atZone(ZoneId.systemDefault()).toInstant());
    }
}
