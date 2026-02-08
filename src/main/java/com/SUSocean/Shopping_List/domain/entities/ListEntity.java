package com.SUSocean.Shopping_List.domain.entities;

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
@Entity
@Table(name = "lists")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "lists_id_uuid")
    private UUID id;

    private Set<String> items;

    @ManyToMany(mappedBy = "lists")
    private Set<UserEntity> users;
}
