package com.codefactory.dev_social_network.discusiones.repository;

import com.codefactory.dev_social_network.discusiones.dto.DiscussionListItem;
import com.codefactory.dev_social_network.discusiones.interfaces.DiscussionCatalogPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DiscussionCatalogAdapter implements DiscussionCatalogPort {

    private final DiscussionRepository discussionRepository;

    public DiscussionCatalogAdapter(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscussionListItem> listRecent() {
        return discussionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(discussion -> new DiscussionListItem(
                        discussion.getId(),
                        discussion.getUserId(),
                        discussion.getTechnologyId(),
                        discussion.getTitle(),
                        discussion.getContent(),
                        discussion.getStatus(),
                        discussion.getCreatedAt()
                ))
                .toList();
    }
}
