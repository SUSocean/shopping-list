package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.ListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ListMapper implements Mapper<ListEntity, ListDto> {

    private ModelMapper modelMapper;

    public ListMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ListDto mapTo(ListEntity listEntity) {
        return modelMapper.map(listEntity, ListDto.class);
    }

    @Override
    public ListEntity mapFrom(ListDto listDto) {
        return modelMapper.map(listDto, ListEntity.class);
    }
}
