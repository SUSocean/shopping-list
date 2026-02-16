package com.SUSocean.Shopping_List;

import com.SUSocean.Shopping_List.domain.dto.*;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;

public final class TestDataUtil {

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

    public static SimpleListDto createSimpleListDtoB() {
        return SimpleListDto.builder()
                .name("Simple List B")
                .build();
    }

    public static RequestItemDto createRequestItemDtoA() {
        return RequestItemDto.builder()
                .name("Item A")
                .build();
    }

    public static RequestItemDto createRequestItemDtoB() {
        return RequestItemDto.builder()
                .name("Item B")
                .build();
    }

    public static ItemDto createInactiveItemDtoB() {
        return ItemDto.builder()
                .name("Item B")
                .active(false)
                .build();
    }
}
