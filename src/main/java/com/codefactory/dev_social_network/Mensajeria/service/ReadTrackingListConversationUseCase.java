package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationReadPort;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListConversationUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Primary
public class ReadTrackingListConversationUseCase implements ListConversationUseCase {

    private final ListConversationUseCase delegate;
    private final ConversationReadPort conversationReadPort;

    public ReadTrackingListConversationUseCase(
            @Qualifier("listConversationUseCaseImpl") ListConversationUseCase delegate,
            ConversationReadPort conversationReadPort) {
        this.delegate = delegate;
        this.conversationReadPort = conversationReadPort;
    }

    @Override
    public List<MessageConversationItem> listar(UUID usuarioAutenticadoId, UUID otroUsuarioId) {
        OffsetDateTime readAt = OffsetDateTime.now();
        List<MessageConversationItem> messages = delegate.listar(usuarioAutenticadoId, otroUsuarioId);
        conversationReadPort.markAsRead(usuarioAutenticadoId, otroUsuarioId, readAt);
        return messages;
    }
}