package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.ItemDto;
import com.SUSocean.Shopping_List.domain.dto.RequestListDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleUserDto;
import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.mappers.impl.ItemMapper;
import com.SUSocean.Shopping_List.repositories.ItemRepository;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.ListService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ListServiceImpl implements ListService {

    private ListRepository listRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;

    public ListServiceImpl(ListRepository listRepository, UserRepository userRepository, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ListEntity createList(SimpleListDto simpleListDto, Long user_id) {

        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = new ListEntity();

        listEntity.setName(simpleListDto.getName());
        listEntity.setCreator(userEntity);
        listEntity.getUsers().add(userEntity);

        userEntity.getLists().add(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity findById(UUID listId, Long userId) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(!listEntity.getUsers().contains(userEntity)){
            throw new RuntimeException("Not a member of a list");
        }

        return listEntity;
    }

    @Override
    @Transactional
    public ListEntity addUser(UUID listId, Long creatorId, SimpleUserDto user) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        UserEntity userEntity = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if(!Objects.equals(listEntity.getCreator().getId(), creatorId)){
            throw new RuntimeException("Not a creator");
        }

        if(listEntity.getUsers().contains(userEntity)){
            throw new RuntimeException("User already added");
        }

        listEntity.getUsers().add(userEntity);
        userEntity.getLists().add(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity removeUser(UUID listId, Long creatorId, SimpleUserDto user) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if(!Objects.equals(listEntity.getCreator().getId(), creatorId)){
            throw new RuntimeException("Not a creator");
        }

        if(!listEntity.getUsers().contains(userEntity)){
            throw new RuntimeException("User not found");
        }

        if(Objects.equals(listEntity.getCreator(), userEntity)){
            listEntity.setCreator(listEntity.getUsers().stream().findFirst().orElseThrow(() -> new RuntimeException("can't remove self")));
        }

        listEntity.getUsers().remove(userEntity);
        userEntity.getLists().remove(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    @Transactional

    public List<ItemDto> editList(Long userId, UUID listId, RequestListDto list) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(!listEntity.getUsers().contains(userEntity)){
            throw new RuntimeException("User not found");
        }

        listEntity.setName(list.getName());

        List<ItemEntity> itemEntities = listEntity.getItems();
        List<UUID> orderedUUIDs = list.getItemsOrder();

        Map<UUID, ItemEntity> itemMap = itemEntities.stream().collect(Collectors.toMap(ItemEntity::getId, Function.identity()));
        for(int i = 0; i < orderedUUIDs.size(); i++){
            ItemEntity item = itemMap.get(orderedUUIDs.get(i));
            item.setPosition(i);
        }

        Collections.sort(listEntity.getItems(), Comparator.comparingInt(ItemEntity::getPosition));

//        ListEntity savedList = listRepository.save(listEntity);
        return listEntity.getItems().stream().map(itemEntity -> itemMapper.mapToItemDto(itemEntity)).toList();
    }
}
