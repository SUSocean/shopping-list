package com.SUSocean.Shopping_List;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;

public final class TestDataUtil {
    public static UserEntity createUserEntityA(){
        return UserEntity.builder()
                .username("Shopper A")
                .password("password A")
                .lists(null)
                .build();
    }

    public static RequestUserDto createRequestUserDtoA(){
        return RequestUserDto.builder()
                .username("Shopper A")
                .password("password A")
                .build();
    }

    public static RequestUserDto createRequestUserDtoB(){
        return RequestUserDto.builder()
                .username("Shopper B")
                .password("password B")
                .build();
    }

    public static SimpleListDto createSimpleListDtoA(){
        return SimpleListDto.builder()
                .name("Simple List A")
                .build();
    }
}
