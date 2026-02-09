package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.domain.dto.ListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.mappers.Mapper;
import com.SUSocean.Shopping_List.services.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListController {

    private ListService listService;

    private Mapper<ListEntity, ListDto> listMapper;

    public ListController(ListService listService, Mapper<ListEntity, ListDto> listMapper) {
        this.listService = listService;
        this.listMapper = listMapper;
    }

//    @PostMapping(path = "/lists/")
//    public ResponseEntity<ListDto> createUser(
//            @RequestBody ListDto listDto)
//    {
//        ListEntity listEntity = listMapper.mapFrom(listDto);
//        ListEntity savedListEntity = listService.createList(listEntity);
//        return new ResponseEntity<>(listMapper.mapTo(savedListEntity), HttpStatus.CREATED);
//    }
}
