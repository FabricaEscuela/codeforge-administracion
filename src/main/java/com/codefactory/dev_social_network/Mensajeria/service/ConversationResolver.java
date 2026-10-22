package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.repository.ConversationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConversationResolver {

    private final ConversationRepository repository;

    public ConversationResolver(ConversationRepository repository) {
        this.repository = repository;
    }

    public ConversationEntity resolve(UUID firstUserId, UUID secondUserId) {
        UUID firstParticipant = firstUserId.compareTo(secondUserId) <= 0 ? firstUserId : secondUserId;
        UUID secondParticipant = firstParticipant.equals(firstUserId) ? secondUserId : firstUserId;
        return repository.findByParticipantOneIdAndParticipantTwoId(firstParticipant, secondParticipant)
                .orElseGet(() -> repository.save(new ConversationEntity(firstParticipant, secondParticipant)));
    }
}
