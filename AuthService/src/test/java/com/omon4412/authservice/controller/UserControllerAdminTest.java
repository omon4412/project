package com.omon4412.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omon4412.authservice.config.SecurityConfig;
import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.RoleDto;
import com.omon4412.authservice.dto.UserDto;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerAdminTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private LoginRequest loginRequestByUser;
    private LoginRequest loginRequestByAdmin;

    private UserDto userDto;
    private UserDto adminDto;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        loginRequestByUser = new LoginRequest("user", "123456g1G+!");
        loginRequestByAdmin = new LoginRequest("admin", "123456g1G+!");
        userDto = new UserDto(1L, "user", "user@4412.ru");
        adminDto = new UserDto(2L, "admin", "admin@4412.ru");
    }

    @Test
    @Transactional
    void test_lockUser_whenUserIsAdmin_correct() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void test_lockUser_whenUserIsNotAdmin_thenReturn401() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByUser);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void test_lockUser_whenUserIsAdminAndUserNotFound_thenReturn404() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1000000000000/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void test_lockUser_whenUserIsAdminAndUserIsLockedAlready_thenReturn400() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/users/1/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void test_unlockUser_whenUserIsAdmin_correct() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/lock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/users/1/unlock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void test_unlockUser_whenUserIsNotAdmin_thenReturn401() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByUser);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/unlock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void test_unlockUser_whenUserIsAdminAndUserNotFound_thenReturn404() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1000000000000/unlock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void test_unlockUser_whenUserIsAdminAndUserIsLockedAlready_thenReturn400() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequestByAdmin);
        MvcResult result = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(adminDto.getId()))
                .andExpect(jsonPath("$.username").value(adminDto.getUsername()))
                .andExpect(jsonPath("$.email").value(adminDto.getEmail()))
                .andExpect(cookie().exists("SESSION"))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        mockMvc.perform(post("/admin/users/1/unlock")
                        .cookie(cookies)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}