package com.codefactory.dev_social_network.Mensajeria.service;

import com.codefactory.dev_social_network.Mensajeria.dto.EnviarMensajeRequest;
import com.codefactory.dev_social_network.Mensajeria.dto.MensajeResponse;
import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import com.codefactory.dev_social_network.Mensajeria.repository.MessageRepository;
import com.codefactory.dev_social_network.bloqueos.interfaces.BloqueoInteraccionPort;
import com.codefactory.dev_social_network.shared.exception.BusinessException;
import com.codefactory.dev_social_network.usuarios.interfaces.UsuarioExistenciaPort;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MensajeService {

    private static final int MAX_CARACTERES = 2000;

    private final MessageRepository messageRepository;
    private final UsuarioExistenciaPort usuarioExistencia;
    private final ConversationResolver conversationResolver;
    private final BloqueoInteraccionPort bloqueoPolicy;

    public MensajeService(
            MessageRepository messageRepository,
            UsuarioExistenciaPort usuarioExistencia,
            ConversationResolver conversationResolver,
            BloqueoInteraccionPort bloqueoPolicy
    ) {
        this.messageRepository = messageRepository;
        this.usuarioExistencia = usuarioExistencia;
        this.conversationResolver = conversationResolver;
        this.bloqueoPolicy = bloqueoPolicy;
    }

    @Transactional
    public MensajeResponse enviarMensaje(
            UUID remitenteId,
            EnviarMensajeRequest request
    ) {
        if (request == null || request.getContenido() == null || request.getContenido().isBlank()) {
            throw new BusinessException(
                    "MENSAJE_INVALIDO",
                    "El contenido del mensaje es obligatorio",
                    HttpStatus.BAD_REQUEST
            );
        }

        String contenido = request.getContenido();
        UUID destinatarioId = request.getDestinatarioId();


        if (contenido.length() > MAX_CARACTERES) {
            throw new BusinessException(
                    "MENSAJE_EXCEDE_LIMITE",
                    "Mensaje excede los 2000 caracteres",
                    HttpStatus.BAD_REQUEST
            );
        }


        if (remitenteId.equals(destinatarioId)) {
            throw new BusinessException(
                    "MENSAJE_A_SI_MISMO",
                    "No puedes enviarte un mensaje a ti mismo",
                    HttpStatus.BAD_REQUEST
            );
        }


        if (destinatarioId == null
                || !usuarioExistencia.existe(destinatarioId)) {
            throw new BusinessException(
                    "USUARIO_NO_EXISTE",
                    "Usuario no existe",
                    HttpStatus.NOT_FOUND
            );
        }

        if (bloqueoPolicy.estaBloqueada(remitenteId, destinatarioId)) {
            throw new BusinessException(
                    "ENVIO_BLOQUEADO",
                    "No es posible enviar mensajes a este usuario",
                    HttpStatus.FORBIDDEN
            );
        }

        MessageEntity mensaje = new MessageEntity(
                remitenteId,
                destinatarioId,
                contenido
        );
        mensaje.setConversation(conversationResolver.resolve(remitenteId, destinatarioId));

        messageRepository.save(mensaje);

        return new MensajeResponse("Mensaje enviado");
    }
}
