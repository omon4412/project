package com.omon4412.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omon4412.authservice.config.NewLoginFilter;
import com.omon4412.authservice.config.SecurityConfig;
import com.omon4412.authservice.dto.LoginRequest;
import com.omon4412.authservice.dto.NewUserRequest;
import com.omon4412.authservice.dto.UserDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class AuthControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private LoginRequest loginRequest;

    @Autowired
    NewLoginFilter newLoginFilter;

    private UserDto userDto;
    private NewUserRequest newUserRequest;

    @Autowired
    private MockMvc mockMvc;

    int lastID = 2;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("user", "123456g1G+!");
        userDto = new UserDto(1L, "user", "user@4412.ru");
        newUserRequest = new NewUserRequest("newUser", "newUser@4412.ru", "123456g1G+!");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void test_createAuthToken_correct() throws Exception {
        String requestBody = mapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/login")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(cookie().exists("SESSION"));
    }

    @Test
    @Transactional
    void test_createAuthToken_whenContentIsNull_thenReturn400() throws Exception {
        mockMvc.perform(post("/login")
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void test_createAuthToken_whenContentIsBlankJSON_thenReturn400() throws Exception {
        mockMvc.perform(post("/login")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void test_createAuthToken_whenUsernameIsWrong_thenReturn401() throws Exception {
        loginRequest.setUsernameOrEmail("wrongUser");
        String wrongLoginRequest = mapper.writeValueAsString(loginRequest);
        mockMvc.perform(post("/login")
                        .content(wrongLoginRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void test_createAuthToken_whenPasswordIsWrong_thenReturn401() throws Exception {
        loginRequest.setPassword("wrongPass");
        String wrongLoginRequest = mapper.writeValueAsString(loginRequest);
        mockMvc.perform(post("/login")
                        .content(wrongLoginRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DirtiesContext
    void test_createNewUser_correct() throws Exception {
        String requestBody = mapper.writeValueAsString(newUserRequest);
        UserDto result = new UserDto(++lastID, newUserRequest.getUsername(), newUserRequest.getEmail());
        mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.username").value(result.getUsername()))
                .andExpect(jsonPath("$.email").value(result.getEmail()));
    }

    @Test
    @Transactional
    @DirtiesContext
    void test_createNewUser_correct_2() throws Exception {
        String requestBody = mapper.writeValueAsString(newUserRequest);
        UserDto result = new UserDto(++lastID, newUserRequest.getUsername(), newUserRequest.getEmail());
        mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.username").value(result.getUsername()))
                .andExpect(jsonPath("$.email").value(result.getEmail()));
    }

    @Test
    void test_createNewUser_whenContentIsNull_thenReturn400() throws Exception {
        String requestBody = mapper.writeValueAsString(null);
        mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_createNewUser_whenContentIsEmptyJSON_thenReturn400() throws Exception {
        mockMvc.perform(post("/register")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_createNewUser_whenUsernameIsExists_thenReturn400() throws Exception {
        newUserRequest.setUsername(userDto.getUsername());
        String requestBody = mapper.writeValueAsString(newUserRequest);
        mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_createNewUser_whenEmailIsExists_thenReturn400() throws Exception {
        newUserRequest.setEmail(userDto.getEmail());
        String requestBody = mapper.writeValueAsString(newUserRequest);
        mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_createNewUser_whenUsernameLengthIs1_thenReturn400() throws Exception {
        newUserRequest.setUsername("a");
        String requestBody = mapper.writeValueAsString(newUserRequest);
        String contentAsString = mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    void test_createNewUser_whenUsernameLengthIsMore260_thenReturn400() throws Exception {
        newUserRequest.setUsername("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Consequat ac felis donec et. Pulvinar sapien et ligula ullamcorper malesuada proin libero nunc consequat. Arcu cursus vitae congue mauris rhoncus aenean. Quis auctor elit sed vulputate mi sit amet mauris commodo. Metus aliquam eleifend mi in nulla. Congue eu consequat");
        String requestBody = mapper.writeValueAsString(newUserRequest);
        String contentAsString = mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    void test_createNewUser_whenUsernameIsBlank_thenReturn400() throws Exception {
        newUserRequest.setUsername(" ");
        String requestBody = mapper.writeValueAsString(newUserRequest);
        String contentAsString = mockMvc.perform(post("/register")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
}