package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.OpenedListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OpenedListMapper {

    private SimpleUserMapper simpleUserMapper;
    private ItemMapper itemMapper;
    public OpenedListMapper(
            SimpleUserMapper simpleUserMapper,
            ItemMapper itemMapper) {
        this.simpleUserMapper = simpleUserMapper;
        this.itemMapper = itemMapper;
    }

    public OpenedListDto mapToOpenedListDto(ListEntity listEntity){
        return OpenedListDto.builder()
                .id(listEntity.getId())
                .name(listEntity.getName())
                .items(listEntity.getItems().stream()
                        .map(itemEntity -> itemMapper.mapToItemDto(itemEntity))
                        .collect(Collectors.toList())
                )
                .users(listEntity.getUsers().stream()
                        .map(userEntity -> simpleUserMapper.mapToSimpleUserDto(userEntity))
                        .collect(Collectors.toSet())
                )
                .creator(simpleUserMapper.mapToSimpleUserDto(listEntity.getCreator()))
                .build();
    }
}
