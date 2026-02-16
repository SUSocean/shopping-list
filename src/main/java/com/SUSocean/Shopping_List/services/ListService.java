package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.dto.*;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;

import java.util.List;
import java.util.UUID;

public interface ListService{
    ListEntity createList(SimpleListDto simpleListDto, Long user_id);

    ListEntity findById(UUID listId, Long userId);

    ListEntity addUser(UUID listId, Long userId, SimpleUserDto user);

    ListEntity removeUser(UUID listId, Long creatorId, SimpleUserDto user);

    List<ItemDto> reorderList(Long userId, UUID listId, RequestReorderListDto list);

    SimpleListDto renameList(Long userId, UUID listId, String name);
}
