package com.SUSocean.Shopping_List.domain.dto;

import com.SUSocean.Shopping_List.domain.entities.ItemEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class OpenedListDto {
    private UUID id;

    private String name;

    private Set<ItemDto> items = new HashSet<>();

    private Set<SimpleUserDto> users = new HashSet<>();

    private SimpleUserDto creator;
}
