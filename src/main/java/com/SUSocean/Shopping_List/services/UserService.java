package com.SUSocean.Shopping_List.services;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserEntity saveUser(RequestUserDto userEntity);

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(Long id);

    UserEntity verifyUser(String username, String password);

    boolean existsByUsename(String username);

    void deleteUser(Long userId);

    ListEntity removeList(Long userId, UUID listId);
}
