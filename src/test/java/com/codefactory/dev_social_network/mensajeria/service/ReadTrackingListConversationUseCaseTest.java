package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.MessageConversationItem;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationReadPort;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ListConversationUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReadTrackingListConversationUseCaseTest {

    private final ListConversationUseCase delegate = mock(ListConversationUseCase.class);
    private final ConversationReadPort readPort = mock(ConversationReadPort.class);
    private final ReadTrackingListConversationUseCase useCase =
            new ReadTrackingListConversationUseCase(delegate, readPort);

    private final UUID me = UUID.randomUUID();
    private final UUID other = UUID.randomUUID();

    @Test
    void alAbrirLaConversacionDevuelveLosMensajesYLosMarcaComoLeidos() {
        List<MessageConversationItem> messages = List.of(new MessageConversationItem(
                UUID.randomUUID(), other, me, "Hola", OffsetDateTime.now()));
        when(delegate.listar(me, other)).thenReturn(messages);

        List<MessageConversationItem> result = useCase.listar(me, other);

        assertThat(result).isEqualTo(messages);
        InOrder order = inOrder(delegate, readPort);
        order.verify(delegate).listar(me, other);
        order.verify(readPort).markAsRead(eq(me), eq(other), any(OffsetDateTime.class));
    }
}