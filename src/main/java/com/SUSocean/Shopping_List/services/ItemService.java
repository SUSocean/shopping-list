package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.dto.ItemDto;
import com.SUSocean.Shopping_List.domain.dto.RequestItemDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;

import java.util.UUID;

public interface ItemService {
    ListEntity createItem(Long userId, UUID listId, RequestItemDto requestItemDto);

    ListEntity removeItem(Long userId, UUID listId, UUID itemId);

    ListEntity toggleItem(Long userId, UUID listId, UUID itemId);
}
