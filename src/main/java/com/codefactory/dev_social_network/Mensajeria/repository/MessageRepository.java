package com.codefactory.dev_social_network.Mensajeria.repository;

import com.codefactory.dev_social_network.Mensajeria.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface MessageRepository
        extends JpaRepository<MessageEntity, UUID> {

    @Query("""
            select message from MessageEntity message
            where (message.remitenteId = :firstUserId and message.destinatarioId = :secondUserId)
               or (message.remitenteId = :secondUserId and message.destinatarioId = :firstUserId)
            order by message.fechaEnvio asc
            """)
    List<MessageEntity> findConversation(
            @Param("firstUserId") UUID firstUserId,
            @Param("secondUserId") UUID secondUserId
    );
}
