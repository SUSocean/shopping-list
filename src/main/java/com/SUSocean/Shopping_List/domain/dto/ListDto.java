package com.SUSocean.Shopping_List.domain.dto;

import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListDto {
    private UUID id;

    private Set<String> items;

    private HashMap<UserEntity, Integer> users;

    private UserEntity creator;
}

