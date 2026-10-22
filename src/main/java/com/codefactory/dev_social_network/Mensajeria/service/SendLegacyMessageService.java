package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacyMessageResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.LegacySendMessageRequest;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.SendLegacyMessageUseCase;
import com.codefactory.dev_social_network.Mensajeria.repository.MessageRepository;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.UserFinderByEmailPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.UUID;

@Service
public class SendLegacyMessageService implements SendLegacyMessageUseCase {

    private final UserFinderByEmailPort userFinder;
    private final ConversationResolver conversationResolver;
    private final MessageRepository messageRepository;

    public SendLegacyMessageService(UserFinderByEmailPort userFinder,
                                    ConversationResolver conversationResolver,
                                    MessageRepository messageRepository) {
        this.userFinder = userFinder;
        this.conversationResolver = conversationResolver;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public LegacyMessageResponse send(UUID senderId, LegacySendMessageRequest request) {
        UserSummary receiver = userFinder.buscarPorEmail(request.receiverEmail().trim())
                .orElseThrow(() -> new BusinessException(
                        "RECIPIENT_NOT_FOUND", "El destinatario no existe", HttpStatus.NOT_FOUND));
        if (senderId.equals(receiver.id())) {
            throw new BusinessException(
                    "SELF_MESSAGE", "No puedes enviarte un mensaje a ti mismo", HttpStatus.BAD_REQUEST);
        }

        ConversationEntity conversation = conversationResolver.resolve(senderId, receiver.id());
        MessageEntity message = new MessageEntity(senderId, receiver.id(), request.content());
        message.setConversation(conversation);
        message = messageRepository.save(message);
        return toResponse(message, conversation.getId());
    }

    private static LegacyMessageResponse toResponse(MessageEntity message, UUID conversationId) {
        return new LegacyMessageResponse(message.getId(), conversationId, message.getRemitenteId(),
                message.getContenido(), message.getFechaEnvio().atZone(ZoneId.systemDefault()).toInstant());
    }
}
