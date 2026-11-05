package com.codefactory.dev_social_network.discusiones.service;

import com.codefactory.dev_social_network.discusiones.dto.DiscussionDetailResponse;
import com.codefactory.dev_social_network.discusiones.entity.Discussion;
import com.codefactory.dev_social_network.discusiones.interfaces.DiscussionFinderPort;
import com.codefactory.dev_social_network.discusiones.interfaces.GetDiscussionUseCase;
import com.codefactory.dev_social_network.shared.exception.DiscusionNoEncontradaException;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.AuthorDirectoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class GetDiscussionUseCaseImpl implements GetDiscussionUseCase {

    private final DiscussionFinderPort discussionFinderPort;
    private final AuthorDirectoryPort authorDirectoryPort;

    public GetDiscussionUseCaseImpl(
            DiscussionFinderPort discussionFinderPort,
            AuthorDirectoryPort authorDirectoryPort
    ) {
        this.discussionFinderPort = discussionFinderPort;
        this.authorDirectoryPort = authorDirectoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public DiscussionDetailResponse getById(UUID discussionId) {

        Discussion discussion = discussionFinderPort.findById(discussionId)
                .orElseThrow(DiscusionNoEncontradaException::new);

        UserSummary author = null;
        if (discussion.getAutorId() != null) {
            Map<UUID, UserSummary> authors = authorDirectoryPort.findByIds(Set.of(discussion.getAutorId()));
            author = authors.get(discussion.getAutorId());
        }

        return new DiscussionDetailResponse(
                discussion.getId(),
                discussion.getTitulo(),
                discussion.getEstado(),
                discussion.getAutorId(),
                author,
                discussion.getContenido(),
                discussion.getIdTecnologia(),
                discussion.getFechaCreacion()
        );
    }
}