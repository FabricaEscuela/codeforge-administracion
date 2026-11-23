package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversationSummary;
import com.codefactory.dev_social_network.Mensajeria.dto.ConversationUnreadSummary;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversationSummaryReaderPort;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.UserFinderByIdPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListMyConversationsUseCaseImplTest {

    private final ConversationSummaryReaderPort readerPort = mock(ConversationSummaryReaderPort.class);
    private final UserFinderByIdPort userFinder = mock(UserFinderByIdPort.class);
    private final ListMyConversationsUseCaseImpl useCase =
            new ListMyConversationsUseCaseImpl(readerPort, userFinder);

    private final UUID me = UUID.randomUUID();
    private final UUID ana = UUID.randomUUID();
    private final UUID leo = UUID.randomUUID();

    @Test
    void muestraLaCantidadDeNoLeidosDeCadaConversacion() {
        when(readerPort.listByParticipant(me)).thenReturn(List.of(
                new ConversationUnreadSummary(UUID.randomUUID(), ana, 3),
                new ConversationUnreadSummary(UUID.randomUUID(), leo, 5)));
        when(userFinder.buscarPorId(ana))
                .thenReturn(Optional.of(new UserSummary(ana, "anadev", "Ana", "Pérez", "ana@correo.com")));
        when(userFinder.buscarPorId(leo))
                .thenReturn(Optional.of(new UserSummary(leo, "leodev", "Leo", "Gómez", "leo@correo.com")));

        List<ConversationSummary> result = useCase.listar(me);

        assertThat(result).extracting(ConversationSummary::otherUsername).containsExactly("anadev", "leodev");
        assertThat(result).extracting(ConversationSummary::unreadCount).containsExactly(3L, 5L);
        assertThat(result.get(0).otherFullName()).isEqualTo("Ana Pérez");
    }

    @Test
    void conversacionSinPendientesMuestraCero() {
        when(readerPort.listByParticipant(me))
                .thenReturn(List.of(new ConversationUnreadSummary(UUID.randomUUID(), ana, 0)));
        when(userFinder.buscarPorId(ana))
                .thenReturn(Optional.of(new UserSummary(ana, "anadev", "Ana", "Pérez", "ana@correo.com")));

        assertThat(useCase.listar(me)).singleElement()
                .satisfies(item -> assertThat(item.unreadCount()).isZero());
    }

    @Test
    void siElOtroUsuarioNoSeEncuentraConservaLaConversacionYElContador() {
        when(readerPort.listByParticipant(me))
                .thenReturn(List.of(new ConversationUnreadSummary(UUID.randomUUID(), ana, 2)));
        when(userFinder.buscarPorId(ana)).thenReturn(Optional.empty());

        assertThat(useCase.listar(me)).singleElement().satisfies(item -> {
            assertThat(item.otherUsername()).isNull();
            assertThat(item.unreadCount()).isEqualTo(2);
        });
    }
}