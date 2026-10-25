package com.codefactory.dev_social_network.discusiones.repository;

import com.codefactory.dev_social_network.discusiones.dto.DiscussionListItem;
import com.codefactory.dev_social_network.discusiones.entity.Discussion;
import com.codefactory.dev_social_network.discusiones.interfaces.DiscussionCatalogPort;
import com.codefactory.dev_social_network.usuarios.dto.UserSummary;
import com.codefactory.dev_social_network.usuarios.interfaces.AuthorDirectoryPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DiscussionCatalogAdapter implements DiscussionCatalogPort {

    private final DiscussionRepository discussionRepository;
    private final AuthorDirectoryPort authorDirectoryPort;

    public DiscussionCatalogAdapter(
            DiscussionRepository discussionRepository,
            AuthorDirectoryPort authorDirectoryPort
    ) {
        this.discussionRepository = discussionRepository;
        this.authorDirectoryPort = authorDirectoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscussionListItem> listRecent() {
        List<Discussion> discussions = discussionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Set<UUID> authorIds = discussions.stream()
                .map(Discussion::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<UUID, UserSummary> authors = authorDirectoryPort.findByIds(authorIds);

        return discussions.stream()
                .map(discussion -> new DiscussionListItem(
                        discussion.getId(),
                        discussion.getUserId(),
                        discussion.getUserId() == null ? null : authors.get(discussion.getUserId()),
                        discussion.getTechnologyId(),
                        discussion.getTitle(),
                        discussion.getContent(),
                        discussion.getStatus(),
                        discussion.getCreatedAt()
                ))
                .toList();
    }
}