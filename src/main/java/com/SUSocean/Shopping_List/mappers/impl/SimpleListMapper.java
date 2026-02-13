package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import org.springframework.stereotype.Component;


@Component
public class SimpleListMapper {

    public SimpleListDto mapToSimpleListDto(ListEntity listEntity){
        return SimpleListDto.builder()
                .id(listEntity.getId())
                .name(listEntity.getName())
                .build();
    }
}
