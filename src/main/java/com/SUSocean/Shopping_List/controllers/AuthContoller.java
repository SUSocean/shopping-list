package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.dto.UserDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.mappers.impl.UserDtoMapper;
import com.SUSocean.Shopping_List.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthContoller {

    UserService userService;
    UserDtoMapper userDtoMapper;

    public AuthContoller(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity<UserDto> login(
            @RequestBody RequestUserDto requestUserDto,
            HttpSession httpSession
    ){
        UserEntity foundUserEntity = userService.verifyUser(
                requestUserDto.getUsername(),
                requestUserDto.getPassword()
        );

        httpSession.setAttribute("userId", foundUserEntity.getId());

        return new ResponseEntity<>(userDtoMapper.mapToUserDto(foundUserEntity),HttpStatus.OK);
    }

    @PostMapping(path = "/auth/logout")
    public ResponseEntity<Void> logout(HttpSession httpSession){

        httpSession.invalidate();

        return ResponseEntity.ok().build();

    }
}
