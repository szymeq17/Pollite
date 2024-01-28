package com.pollite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollite.PolliteTestApplication;
import com.pollite.dto.UserDto;
import com.pollite.model.auth.User;
import com.pollite.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = {PolliteTestApplication.class})
@EnableAutoConfiguration
class AuthControllerIntegrationTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String ECNRYPTED_PASSWORD = "{noop}" + PASSWORD;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void cleanUp() {
        cleanDb();
    }

    @Test
    public void shouldAuthenticateUserSuccessfully() throws Exception {
        //given
        addUser();
        var userDto = UserDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        //when
        var result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        //given
        var userDto = UserDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        //when
        var result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then
        result.andExpect(status().isOk());
        assertThat(userRepository.findByUsername(USERNAME)).isPresent();
    }

    private User addUser() {
        var user = User.builder()
                .username(USERNAME)
                .password(ECNRYPTED_PASSWORD)
                .build();
        return userRepository.save(user);
    }

    private void cleanDb() {
        userRepository.deleteAll();
    }
}