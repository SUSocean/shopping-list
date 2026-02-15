package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.ItemDto;
import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    private SimpleListMapper simpleListMapper;
    public ItemMapper(SimpleListMapper simpleListMapper){
        this.simpleListMapper = simpleListMapper;
    }

    public ItemDto mapToItemDto(ItemEntity itemEntity){
        return ItemDto.builder()
                .id(itemEntity.getId())
                .name(itemEntity.getName())
                .active(itemEntity.isActive())
                .build();
    }
}
