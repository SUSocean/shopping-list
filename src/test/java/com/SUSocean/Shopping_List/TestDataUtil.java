package com.SUSocean.Shopping_List;

import com.SUSocean.Shopping_List.domain.dto.*;
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

    public static SimpleUserDto createSimpleUserDtoA(){
        return SimpleUserDto.builder()
                .id(1L)
                .username("Shopper A")
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

    public static OpenedListDto createOpenedListDtoA(){
        return OpenedListDto.builder()
                .name("List A")
                .creator(createSimpleUserDtoA())
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
