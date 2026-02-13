package com.SUSocean.Shopping_List.repositories;

import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
}
