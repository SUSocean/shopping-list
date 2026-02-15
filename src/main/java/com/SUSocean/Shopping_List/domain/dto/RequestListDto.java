package com.SUSocean.Shopping_List.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestListDto {
    private String name;
    private List<UUID> itemsOrder = new ArrayList<>();
}