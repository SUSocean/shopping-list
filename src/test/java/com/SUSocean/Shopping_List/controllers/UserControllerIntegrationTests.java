package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.TestDataUtil;
import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.entities.ListEntity;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.services.ListService;
import com.SUSocean.Shopping_List.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private UserService userService;

    private ListService listService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTests(
            UserService userService,
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            ListService listService
    ) {
        this.userService = userService;
        this.listService = listService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        String testRequestUserDtoJson = objectMapper.writeValueAsString(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        String testRequestUserDtoJson = objectMapper.writeValueAsString(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testRequestUserDto.getUsername())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.lists").isEmpty()
        );
    }

    @Test
    public void testThatDeleteUserReturnsNoContentAndDeletesHttpSessionAttribute() throws Exception{
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userId", savedUserEntity.getId());

        String testRequestUserDtoJson = objectMapper.writeValueAsString(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users")
                        .sessionAttrs(sessionAttrs)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()
        ).andExpect(MockMvcResultMatchers.request().sessionAttributeDoesNotExist("userId"));
    }

    @Test
    public void testThatCreateListReturnsHttpStatus201Created() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userId", savedUserEntity.getId());

        SimpleListDto simpleListDto = TestDataUtil.createSimpleListDtoA();
        String simpleListDtoJson = objectMapper.writeValueAsString(simpleListDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/lists")
                        .sessionAttrs(sessionAttrs)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simpleListDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateListReturnsSimpleListDtoWhenListCreated() throws Exception{
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        SimpleListDto simpleListDto = TestDataUtil.createSimpleListDtoA();
        String simpleListDtoJson = objectMapper.writeValueAsString(simpleListDto);

        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userId", savedUserEntity.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/lists")
                        .sessionAttrs(sessionAttrs)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simpleListDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").exists()

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(simpleListDto.getName())
        );
    }

    @Test
    public void testThatRemoveListReturnsRemovedSimpleList() throws Exception{
        RequestUserDto requestUserDtoA = TestDataUtil.createRequestUserDtoA();

        UserEntity savedUserEntityA = userService.saveUser(requestUserDtoA);

        SimpleListDto simpleListDtoA = TestDataUtil.createSimpleListDtoA();
        SimpleListDto simpleListDtoB = TestDataUtil.createSimpleListDtoB();

        listService.createList(simpleListDtoA, savedUserEntityA.getId());
        ListEntity savedListEntityB =  listService.createList(simpleListDtoB, savedUserEntityA.getId());

        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userId", savedUserEntityA.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/lists/remove/" + savedListEntityB.getId())
                        .sessionAttrs(sessionAttrs)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedListEntityB.getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedListEntityB.getName())
        );
    }
}
