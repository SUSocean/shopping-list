package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.ItemDto;
import com.SUSocean.Shopping_List.domain.dto.RequestItemDto;
import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.repositories.ItemRepository;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ListRepository listRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    public ItemServiceImpl(
            ListRepository listRepository,
            UserRepository userRepository,
            ItemRepository itemRepository
    ) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ListEntity createItem(Long userId, UUID listId, RequestItemDto requestItemDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!listEntity.getUsers().contains(user)){
            throw new RuntimeException("Not a list member");
        }

        ItemEntity itemEntity = ItemEntity.builder()
                .name(requestItemDto.getName())
                .isActive(true)
                .list(listEntity)
                .build();

        listEntity.getItems().add(itemEntity);

        return listEntity;
    }

    @Override
    @Transactional
    public ListEntity removeItem(Long userId, UUID listId, UUID itemId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!listEntity.getUsers().contains(user)){
            throw new RuntimeException("Not a list member");
        }

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("No item found"));


        if (!itemEntity.getList().getId().equals(listId)) {
            throw new RuntimeException("Item does not belong to this list");
        }

        listEntity.getItems().remove(itemEntity);

        return listEntity;
    }

    @Override
    @Transactional
    public ListEntity toggleItem(Long userId, UUID listId, UUID itemId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!listEntity.getUsers().contains(user)){
            throw new RuntimeException("Not a list member");
        }

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("No item found"));


        if (!itemEntity.getList().getId().equals(listId)) {
            throw new RuntimeException("Item does not belong to this list");
        }

        itemEntity.setActive(!itemEntity.isActive());

        return listEntity;
    }
}
