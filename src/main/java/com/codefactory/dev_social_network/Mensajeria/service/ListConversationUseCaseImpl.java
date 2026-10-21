package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationReaderPort;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListConversationUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListConversationUseCaseImpl implements ListConversationUseCase {

    private final ConversationReaderPort conversationReaderPort;

    public ListConversationUseCaseImpl(ConversationReaderPort conversationReaderPort) {
        this.conversationReaderPort = conversationReaderPort;
    }

    @Override
    public List<MessageConversationItem> listar(UUID usuarioAutenticadoId, UUID otroUsuarioId) {
        return conversationReaderPort.listBetween(usuarioAutenticadoId, otroUsuarioId);
    }
}
