package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.TestDataUtil;
import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.dto.SimpleListDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.services.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTest(UserService userService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.userService = userService;
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
    public void testThatListUsersReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListUsersReturnListOfUsers() throws Exception{
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users").
                contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(savedUserEntity.getUsername())
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].lists").isEmpty());
    }

    @Test
    public void testThatGetUserSuccessfullyReturns200WhenUserExists() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserReturnsUserWhenUserExists() throws Exception{
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedUserEntity.getId())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username").value(savedUserEntity.getUsername())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.lists").isEmpty()
        );
    }
//
//    @Test
//    public void testThatCreateListReturnsHttpStatus201Created() throws Exception {
//        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
//        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);
//
//        String username = objectMapper.writeValueAsString(testRequestUserDto.getUsername());
//        String password = objectMapper.writeValueAsString(testRequestUserDto.getPassword());
//        userService.verifyUser(username, password);
//
//        SimpleListDto simpleListDto = TestDataUtil.createSimpleListDtoA();
//        String simpleListDtoJson = objectMapper.writeValueAsString(simpleListDto);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/users/lists")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(simpleListDtoJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isCreated()
//        );
//    }
//
//    @Test
//    public void testThatCreateListReturnsSimpleListDtoWhenListCreated() throws Exception{
//        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
//        UserEntity savedUserEntity = userService.saveUser(testRequestUserDto);
//
//        SimpleListDto simpleListDto = TestDataUtil.createSimpleListDtoA();
//        String simpleListDtoJson = objectMapper.writeValueAsString(simpleListDto);
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/users/" + savedUserEntity.getId() + "/lists")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(simpleListDtoJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").exists()
//
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.name").value(simpleListDto.getName())
//        );
//    }
}
