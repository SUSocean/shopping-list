package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleUserDto;
import com.SUSocean.Shopping_List.domain.dto.UserDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.mappers.impl.SimpleListMapper;
import com.SUSocean.Shopping_List.mappers.impl.SimpleUserMapper;
import com.SUSocean.Shopping_List.mappers.impl.UserDtoMapper;
import com.SUSocean.Shopping_List.services.ListService;
import com.SUSocean.Shopping_List.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class UserController {

    private UserService userService;
    private ListService listService;

    private UserDtoMapper userDtoMapper;
    private SimpleListMapper simpleListMapper;

    public UserController(UserService userService,
                          UserDtoMapper userDtoMapper,
                          SimpleListMapper simpleListMapper,
                          ListService listService) {
        this.userService = userService;
        this.listService = listService;
        this.userDtoMapper = userDtoMapper;
        this.simpleListMapper = simpleListMapper;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserDto> createUser(
            HttpSession httpSession,
            @RequestBody RequestUserDto user
    ){
        if(userService.existsByUsename(user.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserEntity savedUserEntity = userService.saveUser(user);
        httpSession.setAttribute("userId", savedUserEntity.getId());
        return new ResponseEntity<>(userDtoMapper.mapToUserDto(savedUserEntity), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/users")
    public ResponseEntity<Void> deleteUser(HttpSession httpSession){
        Long userId = (Long) httpSession.getAttribute("userId");

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteUser(userId);

        httpSession.invalidate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/users")
    public List<UserDto> listUsers(){
        List<UserEntity> userEntities = userService.findAll();
        return userEntities.stream()
                .map(userDtoMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id){
        Optional<UserEntity> foundUser = userService.findOne(id);
        return foundUser.map(userEntity -> {
            UserDto userDto = userDtoMapper.mapToUserDto(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/users/lists")
    public ResponseEntity<SimpleListDto> createList(
            HttpSession httpSession,
            @RequestBody SimpleListDto list
    ){
        Long userId = (Long) httpSession.getAttribute("userId");

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ListEntity savedListEntity = listService.createList(list, userId);
        return new ResponseEntity<>(simpleListMapper.mapToSimpleListDto(savedListEntity), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/users/lists/remove/{list_id}")
    public ResponseEntity<SimpleListDto> removeList(
            HttpSession httpSession,
            @PathVariable("list_id") UUID list_id
    ){
        Long userId = (Long) httpSession.getAttribute("userId");

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ListEntity removedListEntity = userService.removeList(userId, list_id);

        return new ResponseEntity<>(simpleListMapper.mapToSimpleListDto(removedListEntity), HttpStatus.OK);
    }
}
