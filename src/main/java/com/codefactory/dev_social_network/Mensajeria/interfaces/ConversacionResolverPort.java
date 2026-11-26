package com.codefactory.dev_social_network.Mensajeria.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;

public interface ConversacionResolverPort {

    Optional<ConversationEntity> find(UUID firstUserId, UUID secondUserId);

    ConversationEntity crear(UUID firstUserId, UUID secondUserId);

    ConversationEntity resolve(UUID firstUserId, UUID secondUserId);
}
