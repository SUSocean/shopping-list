package com.SUSocean.Shopping_List.mappers.impl;

import com.SUSocean.Shopping_List.domain.dto.SimpleUserDto;
import com.SUSocean.Shopping_List.domain.dto.UserDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SimpleUserMapper {

    public SimpleUserDto mapToSimpleUserDto(UserEntity user) {
        return SimpleUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
