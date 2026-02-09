package com.SUSocean.Shopping_List;

import com.SUSocean.Shopping_List.domain.entities.UserEntity;

public final class TestDataUtil {
    public static UserEntity createUserEntityA(){
        return UserEntity.builder()
                .username("Shopper A")
                .password("password A")
                .lists(null)
                .build();
    }
}
