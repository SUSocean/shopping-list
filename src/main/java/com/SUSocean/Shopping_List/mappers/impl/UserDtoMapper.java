package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.UserDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private SimpleListMapper simpleListMapper;
    public UserDtoMapper(ModelMapper modelMapper, SimpleListMapper simpleListMapper) {
        this.simpleListMapper = simpleListMapper;
    }

    public UserDto mapToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .lists(userEntity.getLists().stream()
                        .map(listEntity -> simpleListMapper.mapToSimpleListDto(listEntity))
                        .collect(Collectors.toSet()))
                .build();
    }
}
