package com.SUSocean.Shopping_List.domain.dto;

import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private UUID id;

    private String name;

    private boolean isActive;

    private SimpleListDto list;
}
