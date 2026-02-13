package com.SUSocean.Shopping_List.repositories;

import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ItemRepository extends CrudRepository<ItemEntity, UUID> {
}
