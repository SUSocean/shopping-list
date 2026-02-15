package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.dto.ItemDto;
import com.SUSocean.Shopping_List.domain.dto.RequestItemDto;
import com.SUSocean.Shopping_List.domain.entities.ItemEntity;

import java.util.UUID;

public interface ItemService {
    ItemEntity createItem(Long userId, UUID listId, RequestItemDto requestItemDto);

    ItemEntity removeItem(Long userId, UUID listId, UUID itemId);

    ItemEntity editItem(Long userId, UUID listId, UUID item_id, ItemDto item);
}
