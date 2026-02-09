package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity saveUser(UserEntity userEntity);

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(Long id);
}
