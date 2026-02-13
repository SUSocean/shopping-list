package com.SUSocean.Shopping_List.services.impl;

import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.repositories.ListRepository;
import com.SUSocean.Shopping_List.repositories.UserRepository;
import com.SUSocean.Shopping_List.services.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ListServiceImpl implements ListService {

    private ListRepository listRepository;
    private UserRepository userRepository;

    public ListServiceImpl(ListRepository listRepository, UserRepository userRepository) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ListEntity createList(SimpleListDto simpleListDto, Long user_id) {

        UserEntity userEntity = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListEntity listEntity = new ListEntity();

        listEntity.setName(simpleListDto.getName());
        listEntity.setCreator(userEntity);
        listEntity.getUsers().add(userEntity);

        userEntity.getLists().add(listEntity);

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity findById(UUID listId, Long userId) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(() -> new RuntimeException("List not found"));
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(!listEntity.getUsers().contains(userEntity)){
            throw new RuntimeException("Not a member of a list");
        }

        return listEntity;
    }
}
