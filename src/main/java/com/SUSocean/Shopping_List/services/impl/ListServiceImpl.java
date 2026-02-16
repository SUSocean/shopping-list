package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.*;
import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.exception.BadRequestException;
import com.SUSocean.Shopping_List.exception.ConflictException;
import com.SUSocean.Shopping_List.exception.ForbiddenException;
import com.SUSocean.Shopping_List.exception.NotFoundException;
import com.SUSocean.Shopping_List.mappers.impl.ItemMapper;
import com.SUSocean.Shopping_List.mappers.impl.SimpleListMapper;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ListServiceImpl implements ListService {

    private ListRepository listRepository;
    private UserRepository userRepository;
    private ItemMapper itemMapper;
    private SimpleListMapper simpleListMapper;

    public ListServiceImpl(
            ListRepository listRepository,
            UserRepository userRepository,
            ItemMapper itemMapper,
            SimpleListMapper simpleListMapper
    ) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.simpleListMapper = simpleListMapper;
    }

    @Override
    @Transactional
    public ListEntity createList(SimpleListDto simpleListDto, Long user_id) {

        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ListEntity listEntity = new ListEntity();

        listEntity.setName(simpleListDto.getName());
        listEntity.setCreator(userEntity);
        listEntity.getUsers().add(userEntity);

        userEntity.getLists().add(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ListEntity findById(UUID listId, Long userId) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!listEntity.getUsers().contains(userEntity)){
            throw new ForbiddenException("Not a member of a list");
        }

        return listEntity;
    }

    @Override
    @Transactional
    public ListEntity addUser(UUID listId, Long creatorId, SimpleUserDto user) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));
        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!Objects.equals(listEntity.getCreator().getId(), creatorId)){
            throw new ForbiddenException("Not a creator");
        }

        if(listEntity.getUsers().contains(userEntity)){
            throw new ConflictException("User already added");
        }

        listEntity.getUsers().add(userEntity);
        userEntity.getLists().add(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity removeUser(UUID listId, Long creatorId, SimpleUserDto user) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!Objects.equals(listEntity.getCreator().getId(), creatorId)){
            throw new ForbiddenException("Not a creator");
        }

        if(!listEntity.getUsers().contains(userEntity)){
            throw new NotFoundException("User not found");
        }

        if(Objects.equals(listEntity.getCreator(), userEntity)){
            listEntity.setCreator(listEntity.getUsers().stream().findFirst()
                    .orElseThrow(() -> new BadRequestException("can't remove self")));
        }

        listEntity.getUsers().remove(userEntity);
        userEntity.getLists().remove(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    @Transactional
    public List<ItemDto> reorderList(Long userId, UUID listId, RequestReorderListDto list) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!listEntity.getUsers().contains(userEntity)){
            throw new NotFoundException("User not found");
        }

        List<ItemEntity> itemEntities = listEntity.getItems();
        List<UUID> orderedUUIDs = list.getItemsOrder();

        Map<UUID, ItemEntity> itemMap = itemEntities.stream()
                .collect(Collectors.toMap(ItemEntity::getId, Function.identity()));
        for(int i = 0; i < orderedUUIDs.size(); i++){
            ItemEntity item = itemMap.get(orderedUUIDs.get(i));
            item.setPosition(i);
        }

        listEntity.getItems().sort(Comparator.comparingInt(ItemEntity::getPosition));

        return listEntity.getItems().stream()
                .map(itemEntity -> itemMapper.mapToItemDto(itemEntity)).toList();
    }

    @Override
    public SimpleListDto renameList(Long userId, UUID listId, String name) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!Objects.equals(listEntity.getCreator(), userEntity)){
            throw new ForbiddenException("Not a creator");
        }

        listEntity.setName(name);

        return simpleListMapper.mapToSimpleListDto(listEntity);
    }
}
