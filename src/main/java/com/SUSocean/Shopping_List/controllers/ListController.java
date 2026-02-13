package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.domain.dto.OpenedListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.mappers.impl.OpenedListMapper;
import com.SUSocean.Shopping_List.services.ListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ListController {

    private ListService listService;

    private OpenedListMapper openedListMapper;

    public ListController(
            ListService listService,
            OpenedListMapper openedListMapper
    ) {
        this.listService = listService;
        this.openedListMapper = openedListMapper;
    }

    @GetMapping(path = "/lists/{list_id}")
    public ResponseEntity<OpenedListDto> getList(
            HttpSession httpSession,
            @PathVariable("list_id") UUID list_id
    ){
        Long userId = (Long) httpSession.getAttribute("userId");

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ListEntity listEntity = listService.findById(list_id, userId);

        return new ResponseEntity<>(openedListMapper.mapToOpenedListDto(listEntity), HttpStatus.OK);
    }
}
