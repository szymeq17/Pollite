package com.pollite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollite.PolliteTestApplication;
import com.pollite.dto.PollAnswerDto;
import com.pollite.dto.PollDto;
import com.pollite.model.Poll;
import com.pollite.model.PollAnswer;
import com.pollite.model.auth.User;
import com.pollite.repository.PollAnswerRepository;
import com.pollite.repository.PollRepository;
import com.pollite.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = {PolliteTestApplication.class})
public class PollControllerIntegrationTest {

    private static final String USERNAME = "user";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollAnswerRepository pollAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        addUser();
    }

    @AfterEach
    public void cleanUp() {
        cleanDb();
    }

    @Test
    @WithMockUser(username = USERNAME)
    @Transactional
    public void shouldAddPoll() throws Exception {
        //given
        var pollDto = PollDto.builder()
                .text("Question")
                .pollAnswers(List.of(
                        PollAnswerDto.builder().text("Answer 1").build(),
                        PollAnswerDto.builder().text("Answer 2").build(),
                        PollAnswerDto.builder().text("Answer 3").build()
                ))
                .build();

        //when
        var result = mockMvc.perform(post("/api/poll/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(pollDto)));

        //then
        result.andExpect(status().isCreated());
        var polls = pollRepository.findAll();
        assertThat(polls).hasSize(1);

        var poll = polls.get(0);
        assertThat(poll.getText()).isEqualTo("Question");
        assertThat(poll.getOwner().getUsername()).isEqualTo(USERNAME);

        var answer1 = poll.getPollAnswers().get(0);
        var answer2 = poll.getPollAnswers().get(1);
        var answer3 = poll.getPollAnswers().get(2);

        assertThat(answer1.getText()).isEqualTo("Answer 1");
        assertThat(answer1.getVotesTotal()).isEqualTo(0);
        assertThat(answer2.getText()).isEqualTo("Answer 2");
        assertThat(answer2.getVotesTotal()).isEqualTo(0);
        assertThat(answer3.getText()).isEqualTo("Answer 3");
        assertThat(answer3.getVotesTotal()).isEqualTo(0);
    }

    private void cleanDb() {
        pollRepository.deleteAll();
        pollAnswerRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void addUser() {
        var user = User.builder()
                .username(USERNAME)
                .password("password")
                .build();
        userRepository.save(user);
    }
}
