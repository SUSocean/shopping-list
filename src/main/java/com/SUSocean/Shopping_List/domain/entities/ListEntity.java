package com.SUSocean.Shopping_List.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lists")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "lists_id_uuid")
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<ItemEntity> items = new ArrayList<>();

    @ManyToMany(mappedBy = "lists")
    private Set<UserEntity> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = true)
    private UserEntity creator;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListEntity)) return false;
        ListEntity that = (ListEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
