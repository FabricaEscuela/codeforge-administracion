package com.codefactory.dev_social_network.discusiones.interfaces;

import com.codefactory.dev_social_network.discusiones.dto.DiscussionListItem;

import java.util.List;

public interface DiscussionCatalogPort {
    List<DiscussionListItem> listRecent();
}
