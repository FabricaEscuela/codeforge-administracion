package com.codefactory.dev_social_network.Mensajeria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations", uniqueConstraints = @UniqueConstraint(
        name = "uk_conversation_participants",
        columnNames = {"participant_one_id", "participant_two_id"}))
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "participant_one_id", nullable = false)
    private UUID participantOneId;

    @Column(name = "participant_two_id", nullable = false)
    private UUID participantTwoId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected ConversationEntity() {
    }

    public ConversationEntity(UUID participantOneId, UUID participantTwoId) {
        this.participantOneId = participantOneId;
        this.participantTwoId = participantTwoId;
    }

    @PrePersist
    protected void assignCreationTime() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getParticipantOneId() {
        return participantOneId;
    }

    public UUID getParticipantTwoId() {
        return participantTwoId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
