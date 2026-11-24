package com.codefactory.dev_social_network.Mensajeria.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversation_read_states", uniqueConstraints = @UniqueConstraint(
        name = "uq_read_state_conversation_user",
        columnNames = {"id_conversacion", "id_usuario"}))
public class ConversationReadStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conversacion", nullable = false)
    private ConversationEntity conversation;

    @Column(name = "id_usuario", nullable = false)
    private UUID userId;

    @Column(name = "fecha_ultima_lectura", nullable = false)
    private OffsetDateTime lastReadAt;

    protected ConversationReadStateEntity() {
    }

    public ConversationReadStateEntity(ConversationEntity conversation, UUID userId, OffsetDateTime lastReadAt) {
        this.conversation = conversation;
        this.userId = userId;
        this.lastReadAt = lastReadAt;
    }


    public void advanceTo(OffsetDateTime readAt) {
        if (readAt.isAfter(lastReadAt)) {
            lastReadAt = readAt;
        }
    }

    public UUID getId() { return id; }
    public ConversationEntity getConversation() { return conversation; }
    public UUID getUserId() { return userId; }
    public OffsetDateTime getLastReadAt() { return lastReadAt; }
}