package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.exception.InvalidCredentialsException;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ListRepository listRepository;
    public UserServiceImpl(UserRepository userRepository, ListRepository listRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    @Override
    public UserEntity saveUser(RequestUserDto user) {
        UserEntity userEntity = UserEntity.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .lists(new HashSet<ListEntity>())
                .build();

        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAll() {
        return StreamSupport.stream(userRepository.
                findAll()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity verifyUser(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if(!Objects.equals(user.getPassword(), password)) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    @Override
    public boolean existsByUsename(String username) {
        if(userRepository.findByUsername(username).isEmpty()){
            return false;
        } else{
            return true;
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity userEntity =  userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));

        userEntity.getLists().forEach(listEntity -> {

            listEntity.getUsers().remove(userEntity);

            if(Objects.equals(listEntity.getCreator(), userEntity)){

                if(listEntity.getUsers().isEmpty()){
                    listRepository.delete(listEntity);
                }else{
                    listEntity.setCreator(listEntity.getUsers().stream().findFirst()
                            .orElseThrow(() -> new RuntimeException("No users")));
                }

            }
        });

        userRepository.deleteById(userId);
    }

}
