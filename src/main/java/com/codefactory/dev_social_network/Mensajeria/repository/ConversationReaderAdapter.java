package com.codefactory.dev_social_network.Mensajeria.repository;

import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationReaderPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class ConversationReaderAdapter implements ConversationReaderPort {

    private final MessageRepository messageRepository;

    public ConversationReaderAdapter(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageConversationItem> listBetween(UUID firstUserId, UUID secondUserId) {
        return messageRepository.findConversation(firstUserId, secondUserId)
                .stream()
                .map(message -> new MessageConversationItem(
                        message.getId(),
                        message.getRemitenteId(),
                        message.getDestinatarioId(),
                        message.getContenido(),
                        message.getFechaEnvio()
                ))
                .toList();
    }
}
