package com.SUSocean.Shopping_List.repositories;

import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends CrudRepository<ItemEntity, UUID>, JpaRepository<ItemEntity, UUID> {
    @Query("SELECT MAX(i.position) FROM ItemEntity i WHERE i.list.id = :listId")
    Optional<Integer> findMaxPositionByListId(@Param("listId") UUID listId);
}
