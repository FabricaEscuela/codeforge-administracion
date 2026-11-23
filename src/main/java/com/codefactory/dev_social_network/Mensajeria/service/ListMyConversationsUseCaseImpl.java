package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationSummary;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationSummaryReaderPort;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListMyConversationsUseCase;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.UserFinderByIdPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListMyConversationsUseCaseImpl implements ListMyConversationsUseCase {

    private final ConversationSummaryReaderPort conversationSummaryReaderPort;
    private final UserFinderByIdPort userFinderByIdPort;

    public ListMyConversationsUseCaseImpl(ConversationSummaryReaderPort conversationSummaryReaderPort,
                                          UserFinderByIdPort userFinderByIdPort) {
        this.conversationSummaryReaderPort = conversationSummaryReaderPort;
        this.userFinderByIdPort = userFinderByIdPort;
    }

    @Override
    public List<ConversationSummary> listar(UUID usuarioId) {
        return conversationSummaryReaderPort.listByParticipant(usuarioId).stream()
                .map(row -> {
                    Optional<UserSummary> other = userFinderByIdPort.buscarPorId(row.otherUserId());
                    return new ConversationSummary(
                            row.conversationId(),
                            row.otherUserId(),
                            other.map(UserSummary::username).orElse(null),
                            other.map(user -> (user.nombre() + " " + user.apellido()).trim()).orElse(null),
                            row.unreadCount());
                })
                .toList();
    }
}