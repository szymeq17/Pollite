package com.pollite.controller;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = {PolliteTestApplication.class})
@EnableAutoConfiguration
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
        addUser();
        var pollDto = PollDto.builder()
                .text("Question")
                .pollAnswers(List.of(
                        PollAnswerDto.builder().text("Answer 1").build(),
                        PollAnswerDto.builder().text("Answer 2").build(),
                        PollAnswerDto.builder().text("Answer 3").build()
                ))
                .build();

        //when
        var result = mockMvc.perform(post("/api/polls/")
                        .contentType(MediaType.APPLICATION_JSON)
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

    @Test
    public void shouldGetPolls() throws Exception {
        //given
        var user = addUser();
        var poll1 = createAndSavePoll(user);
        var poll2 = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(get("/api/polls/")
                        .contentType("application/json"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(poll1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(poll2.getId()));
    }

    @Test
    public void shouldGetPoll() throws Exception {
        //given
        var user = addUser();
        var poll = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(get("/api/polls/{id}", poll.getId())
                        .contentType("application/json"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(poll.getId()))
                .andExpect(jsonPath("$.ownerUsername").value(poll.getOwner().getUsername()))
                .andExpect(jsonPath("$.text").value(poll.getText()))
                .andExpect(jsonPath("$.pollAnswers[0].text").value(poll.getPollAnswers().get(0).getText()))
                .andExpect(jsonPath("$.pollAnswers[1].text").value(poll.getPollAnswers().get(1).getText()));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldGetUsersPollInfos() throws Exception {
        //given
        var user = addUser();
        var poll = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(get("/api/polls/users/{username}", USERNAME)
                        .contentType("application/json"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(poll.getId()))
                .andExpect(jsonPath("$.content[0].text").value(poll.getText()));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldDeletePoll() throws Exception {
        //given
        var user = addUser();
        var poll = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(delete("/api/polls/{pollId}", poll.getId())
                        .contentType("application/json"));

        //then
        result.andExpect(status().isOk());
        assertThat(pollRepository.findById(poll.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldGetPollResults() throws Exception {
        //given
        var user = addUser();
        var poll = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(get("/api/polls/{pollId}/results", poll.getId())
                .contentType("application/json"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.votesTotal").value(10))
                .andExpect(jsonPath("$.results[0].pollAnswerText").value(poll.getPollAnswers().get(0).getText()))
                .andExpect(jsonPath("$.results[0].votes").value(poll.getPollAnswers().get(0).getVotesTotal()))
                .andExpect(jsonPath("$.results[0].percentage").value(80))
                .andExpect(jsonPath("$.results[1].pollAnswerText").value(poll.getPollAnswers().get(1).getText()))
                .andExpect(jsonPath("$.results[1].votes").value(poll.getPollAnswers().get(1).getVotesTotal()))
                .andExpect(jsonPath("$.results[1].percentage").value(20));

    }

    @Test
    public void shouldSaveVoteProperly() throws Exception {
        //given
        var user = addUser();
        var poll = createAndSavePoll(user);

        //when
        var result = mockMvc.perform(
                post("/api/polls/{pollId}/vote/{pollAnswerId}",
                        poll.getId(),
                        poll.getPollAnswers().get(0).getId())
                .contentType("application/json"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.votesTotal").value(11))
                .andExpect(jsonPath("$.results[0].pollAnswerText").value(poll.getPollAnswers().get(0).getText()))
                .andExpect(jsonPath("$.results[0].votes").value(poll.getPollAnswers().get(0).getVotesTotal() + 1))
                .andExpect(jsonPath("$.results[0].percentage").value(BigDecimal.valueOf(81.82)))
                .andExpect(jsonPath("$.results[1].pollAnswerText").value(poll.getPollAnswers().get(1).getText()))
                .andExpect(jsonPath("$.results[1].votes").value(poll.getPollAnswers().get(1).getVotesTotal()))
                .andExpect(jsonPath("$.results[1].percentage").value(BigDecimal.valueOf(18.18)));
    }

    private void cleanDb() {
        pollRepository.deleteAll();
        pollAnswerRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Poll createAndSavePoll(User user) {
        var poll = Poll.builder()
                .text("Question")
                .pollAnswers(List.of(
                        PollAnswer.builder().text("Answer 1").votesTotal(8L).build(),
                        PollAnswer.builder().text("Answer 2").votesTotal(2L).build()
                ))
                .owner(user)
                .build();

        return pollRepository.save(poll);
    }


    private User addUser() {
        var user = User.builder()
                .username(USERNAME)
                .password("password")
                .build();
        return userRepository.save(user);
    }
}
