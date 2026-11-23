package com.codefactory.dev_social_network.Mensajeria.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.Mensajeria.dto.ConversacionResponse;
import com.codefactory.dev_social_network.Mensajeria.dto.InicioConversacionResultado;
import com.codefactory.dev_social_network.Mensajeria.entity.ConversationEntity;
import com.codefactory.dev_social_network.Mensajeria.interfaces.ConversacionResolverPort;
import com.codefactory.dev_social_network.Mensajeria.interfaces.IniciarConversacionUseCase;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

@Service
public class IniciarConversacionService implements IniciarConversacionUseCase {

    private final BloqueoInteraccionPort bloqueoPolicy;
    private final UsuarioExistenciaPort usuarioExistencia;
    private final ConversacionResolverPort conversationResolver;

    public IniciarConversacionService(BloqueoInteraccionPort bloqueoPolicy,
                                      UsuarioExistenciaPort usuarioExistencia,
                                      ConversacionResolverPort conversationResolver) {
        this.bloqueoPolicy = bloqueoPolicy;
        this.usuarioExistencia = usuarioExistencia;
        this.conversationResolver = conversationResolver;
    }

    @Override
    @Transactional
    public InicioConversacionResultado iniciar(UUID usuarioId, UUID otroUsuarioId) {
        if (bloqueoPolicy.estaBloqueada(usuarioId, otroUsuarioId)) {
            throw new BusinessException(
                    "CONVERSACION_BLOQUEADA",
                    "No es posible iniciar una conversación con este usuario",
                    HttpStatus.FORBIDDEN
            );
        }

        if (otroUsuarioId == null || !usuarioExistencia.existe(otroUsuarioId)) {
            throw new BusinessException(
                    "USUARIO_NO_EXISTE",
                    "El usuario no existe",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<ConversationEntity> existente = conversationResolver.find(usuarioId, otroUsuarioId);

        if (existente.isPresent()) {
            return new InicioConversacionResultado(
                    new ConversacionResponse(existente.get().getId()),
                    false
            );
        }

        ConversationEntity creada = conversationResolver.crear(usuarioId, otroUsuarioId);

        return new InicioConversacionResultado(
                new ConversacionResponse(creada.getId()),
                true
        );
    }
}
