package com.omon4412.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omon4412.authservice.config.SecurityConfig;
import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.RoleDto;
import com.omon4412.authservice.dto.UserFullDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerPrivateTest {

    UserFullDto userFullDto;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("user", "123456g1G+!");
        userFullDto = new UserFullDto(1L, "user", "user@4412.ru", new ArrayList<>() {{
            add(new RoleDto("ROLE_USER"));
        }});
    }

    @Test
    @Transactional
    void test_getCurrentUserInfo_correct() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userFullDto.getId()))
                .andExpect(jsonPath("$.username").value(userFullDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userFullDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(get("/user")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userFullDto.getId()))
                .andExpect(jsonPath("$.username").value(userFullDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userFullDto.getEmail()))
                .andExpect(jsonPath("$.roles[*].name", containsInAnyOrder(userFullDto.getRoles().stream().map(RoleDto::getName).toArray())));
    }
}