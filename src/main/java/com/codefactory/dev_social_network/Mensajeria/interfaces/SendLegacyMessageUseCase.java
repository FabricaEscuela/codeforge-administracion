package com.codefactory.dev_social_network.Mensajeria.interfaces;

import com.codefactory.dev_social_network.Mensajeria.dto.LegacyMessageResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.LegacySendMessageRequest;

import java.util.UUID;

public interface SendLegacyMessageUseCase {
    LegacyMessageResponse send(UUID senderId, LegacySendMessageRequest request);
}
