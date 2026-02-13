package com.SUSocean.Shopping_List.controllers;

import com.SUSocean.Shopping_List.TestDataUtil;
import com.SUSocean.Shopping_List.domain.dto.RequestUserDto;
import com.SUSocean.Shopping_List.domain.entities.UserEntity;
import com.SUSocean.Shopping_List.services.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthControllerIntegrationTest(UserService userService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatAuthLoginReturns200OkWhenCredentialsAreCorrect() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        userService.saveUser(testRequestUserDto);

        String testRequestUserDtoJson = objectMapper.writeValueAsString(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatAuthLoginReturnsCorrectUserDtoWhenCredentialsAreCorrect() throws Exception {
        RequestUserDto testRequestUserDto = TestDataUtil.createRequestUserDtoA();
        UserEntity savedUser = userService.saveUser(testRequestUserDto);

        String testRequestUserDtoJson = objectMapper.writeValueAsString(testRequestUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.username").value(savedUser.getUsername())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.lists").isEmpty()
        );
    }

    @Test
    public void testThatAuthLoginReturns401UnauthorizedWhenCredentialAreIncorrect() throws Exception {
        RequestUserDto testRequestUserDtoA = TestDataUtil.createRequestUserDtoA();
        RequestUserDto testRequestUserDtoB = TestDataUtil.createRequestUserDtoB();
        userService.saveUser(testRequestUserDtoA);
        String testRequestUserDtoBJson = objectMapper.writeValueAsString(testRequestUserDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequestUserDtoBJson)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testThatAuthLogoutReturns200Ok() throws Exception {mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/logout")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
