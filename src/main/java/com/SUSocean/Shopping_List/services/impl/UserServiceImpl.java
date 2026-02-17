package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.exception.BadRequestException;
import com.SUSocean.Shopping_List.exception.ConflictException;
import com.SUSocean.Shopping_List.exception.InvalidCredentialsException;
import com.SUSocean.Shopping_List.exception.NotFoundException;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            ListRepository listRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity saveUser(RequestUserDto user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new ConflictException("User with name already exist");
        }

        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode(user.getPassword()))
                .username(user.getUsername())
                .lists(new HashSet<>())
                .build();

        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity verifyUser(String username, String password) {

        if(username.isBlank() || password.isBlank()){
            throw new BadRequestException("Blank username or password");
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username.isBlank()){
            throw new BadRequestException("Empty username");
        }
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null){
            throw new BadRequestException("Wrong user id");
        }

        UserEntity userEntity =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found"));

        userEntity.getLists().forEach(listEntity -> {

            listEntity.getUsers().remove(userEntity);

            if(Objects.equals(listEntity.getCreator(), userEntity)){

                if(listEntity.getUsers().isEmpty()){
                    listRepository.delete(listEntity);
                }else{
                    listEntity.setCreator(listEntity.getUsers().stream().findFirst()
                            .orElseThrow(() -> new NotFoundException("No users")));
                }

            }
        });

        userRepository.deleteById(userId);
    }

    @Override
    public ListEntity removeList(Long userId, UUID listId) {

        if(userId == null || listId == null){
            throw new BadRequestException("wrong user or list id");
        }

        UserEntity userEntity =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found"));
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));

        userEntity.getLists().remove(listEntity);
        listEntity.getUsers().remove(userEntity);

        if (Objects.equals(listEntity.getCreator(), userEntity)){
            if (listEntity.getUsers().isEmpty()){
                listRepository.delete(listEntity);
            }else{
                listEntity.setCreator(
                        listEntity.getUsers().stream().findFirst()
                        .orElseThrow(() -> new NotFoundException("No users"))
                );
            }
        }

        userRepository.save(userEntity);

        return listEntity;
    }
}
