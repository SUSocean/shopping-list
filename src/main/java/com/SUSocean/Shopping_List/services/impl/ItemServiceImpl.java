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

import java.util.UUID;

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
    public ItemEntity createItem(Long userId, UUID listId, RequestItemDto requestItemDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!listEntity.getUsers().contains(user)){
            throw new RuntimeException("Not a list member");
        }

        int nextPosition = itemRepository.findMaxPositionByListId(listId).orElse(-1) + 1;

        ItemEntity itemEntity = ItemEntity.builder()
                .name(requestItemDto.getName())
                .isActive(true)
                .list(listEntity)
                .position(nextPosition)
                .build();

        listEntity.getItems().add(itemEntity);

        itemRepository.flush();
        return itemEntity;
    }

    @Override
    @Transactional
    public ItemEntity removeItem(Long userId, UUID listId, UUID itemId) {
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

        int remmovedPosition = itemEntity.getPosition();

        listEntity.getItems().remove(itemEntity);

        for(ItemEntity item : listEntity.getItems()){
            if(item.getPosition() > remmovedPosition){
                item.setPosition(item.getPosition() - 1);
            }
        }

        return itemEntity;
    }

    @Override
    @Transactional
    public ItemEntity editItem(Long userId, UUID listId, UUID item_id, ItemDto item) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!listEntity.getUsers().contains(user)){
            throw new RuntimeException("Not a list member");
        }

        ItemEntity itemEntity = itemRepository.findById(item_id)
                .orElseThrow(() -> new RuntimeException("No item found"));


        if (!itemEntity.getList().getId().equals(listId)) {
            throw new RuntimeException("Item does not belong to this list");
        }

        itemEntity.setActive(item.isActive());
        itemEntity.setName(item.getName());

        return itemEntity;
    }
}
