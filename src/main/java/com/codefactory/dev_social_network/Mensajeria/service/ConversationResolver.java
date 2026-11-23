package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversacionResolverPort;
import com.codefactory.dev_social_network.Mensajeria.repository.ConversationRepository;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ConversationResolver implements ConversacionResolverPort {

    private final ConversationRepository repository;
    private final BloqueoInteraccionPort bloqueoPolicy;

    public ConversationResolver(ConversationRepository repository,
                               BloqueoInteraccionPort bloqueoPolicy) {
        this.repository = repository;
        this.bloqueoPolicy = bloqueoPolicy;
    }

    @Override
    public ConversationEntity resolve(UUID firstUserId, UUID secondUserId) {
        if (bloqueoPolicy.estaBloqueada(firstUserId, secondUserId)) {
            throw new BusinessException(
                    "CONVERSACION_BLOQUEADA",
                    "No es posible iniciar una conversación con este usuario",
                    HttpStatus.FORBIDDEN
            );
        }

        return find(firstUserId, secondUserId)
                .orElseGet(() -> crear(firstUserId, secondUserId));
    }

    @Override
    public Optional<ConversationEntity> find(UUID firstUserId, UUID secondUserId) {
        UUID firstParticipant = firstUserId.compareTo(secondUserId) <= 0 ? firstUserId : secondUserId;
        UUID secondParticipant = firstParticipant.equals(firstUserId) ? secondUserId : firstUserId;
        return repository.findByParticipantOneIdAndParticipantTwoId(firstParticipant, secondParticipant);
    }

    @Override
    public ConversationEntity crear(UUID firstUserId, UUID secondUserId) {
        UUID firstParticipant = firstUserId.compareTo(secondUserId) <= 0 ? firstUserId : secondUserId;
        UUID secondParticipant = firstParticipant.equals(firstUserId) ? secondUserId : firstUserId;
        return repository.save(new ConversationEntity(firstParticipant, secondParticipant));
    }
}
