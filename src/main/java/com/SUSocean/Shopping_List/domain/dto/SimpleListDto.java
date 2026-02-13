package com.SUSocean.Shopping_List.domain.dto;

import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleListDto {
    private UUID id;

    private String name;
}

