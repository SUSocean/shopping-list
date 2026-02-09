package com.SUSocean.Shopping_List.repositories;

import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ListRepository extends CrudRepository<ListEntity, UUID> {
}
