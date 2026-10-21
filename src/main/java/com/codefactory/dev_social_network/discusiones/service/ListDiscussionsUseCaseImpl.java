package com.codefactory.dev_social_network.discusiones.service;

import com.codefactory.dev_social_network.discusiones.dto.DiscussionListItem;
import com.codefactory.dev_social_network.discusiones.interfaces.DiscussionCatalogPort;
import com.codefactory.dev_social_network.discusiones.interfaces.ListDiscussionsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListDiscussionsUseCaseImpl implements ListDiscussionsUseCase {

    private final DiscussionCatalogPort discussionCatalogPort;

    public ListDiscussionsUseCaseImpl(DiscussionCatalogPort discussionCatalogPort) {
        this.discussionCatalogPort = discussionCatalogPort;
    }

    @Override
    public List<DiscussionListItem> listRecent() {
        return discussionCatalogPort.listRecent();
    }
}
