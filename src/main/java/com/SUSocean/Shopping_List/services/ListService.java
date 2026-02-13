package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleUserDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;

import java.util.UUID;

public interface ListService{
    ListEntity createList(SimpleListDto simpleListDto, Long user_id);

    ListEntity findById(UUID listId, Long userId);

    ListEntity addUser(UUID listId, Long userId, SimpleUserDto user);

    ListEntity removeUser(UUID listId, Long creatorId, SimpleUserDto user);
}
