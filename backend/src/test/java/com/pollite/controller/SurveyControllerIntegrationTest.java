package com.pollite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollite.PolliteTestApplication;
import com.pollite.dto.*;
import com.pollite.model.auth.User;
import com.pollite.model.survey.CompletedSurvey;
import com.pollite.model.survey.Survey;
import com.pollite.model.survey.SurveyConfiguration;
import com.pollite.model.survey.SurveyQuestionExclusion;
import com.pollite.model.survey.answer.SurveyQuestionAnswer;
import com.pollite.model.survey.question.CompletedQuestion;
import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.model.survey.question.SurveyQuestionType;
import com.pollite.repository.CompletedSurveyRepository;
import com.pollite.repository.SurveyRepository;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = {PolliteTestApplication.class})
@EnableAutoConfiguration
class SurveyControllerIntegrationTest {

    private static final String USERNAME = "user";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CompletedSurveyRepository completedSurveyRepository;

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
    public void shouldCreateSurvey() throws Exception {
        //given
        addUser();
        var surveyDto = createSurveyDto();

        //when
        var result = mockMvc.perform(post("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(surveyDto)));

        //then
        result.andExpect(status().isCreated());

        var surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(1);
    }

    @Test
    public void shouldGetAllSurveyInfos() throws Exception {
        //given
        var user = addUser();
        var survey1 = createAndSaveSurvey(user);
        var survey2 = createAndSaveSurvey(user);

        //when
        var result = mockMvc.perform(get("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].surveyId").value(survey1.getId()))
                .andExpect(jsonPath("$.content[1].surveyId").value(survey2.getId()));
    }

    @Test
    public void shouldGetSurvey() throws Exception {
        //given
        var user = addUser();
        var survey = createAndSaveSurvey(user);

        //when
        var result = mockMvc.perform(get("/api/surveys/{surveyId}", survey.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(survey.getId()));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldDeleteSurvey() throws Exception {
        //given
        var user = addUser();
        var survey = createAndSaveSurvey(user);

        //when
        var result = mockMvc.perform(delete("/api/surveys/{surveyId}", survey.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());

        assertThat(surveyRepository.findById(survey.getId())).isEmpty();
    }

    @Test
    public void shouldSubmitCompletedSurvey() throws Exception {
        //given
        var user = addUser();
        var survey = createAndSaveSurvey(user);
        var completedSurveyDto = createCompletedSurveyDto(survey);

        //when
        var result = mockMvc.perform(post("/api/surveys/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completedSurveyDto)));

        //then
        result.andExpect(status().isCreated());

        var completedSurveys = completedSurveyRepository.findAllBySurveyId(survey.getId());
        assertThat(completedSurveys).hasSize(1);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldGetSurveyResults() throws Exception {
        //given
        var user = addUser();
        var survey = createAndSaveSurvey(user);
        createAndSaveCompletedSurvey(survey);
        createAndSaveCompletedSurvey(survey);

        //when
        var result = mockMvc.perform(post("/api/surveys/{surveyId}/results", survey.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.questionsResults[0].questionId").value(survey.getQuestions().get(0).getId()))
                .andExpect(jsonPath("$.questionsResults[0].total").value(2))
                .andExpect(jsonPath("$.questionsResults[0].answersResults[0].total").value(2))
                .andExpect(jsonPath("$.questionsResults[0].answersResults[1].total").value(0))
                .andExpect(jsonPath("$.questionsResults[1].questionId").value(survey.getQuestions().get(1).getId()))
                .andExpect(jsonPath("$.questionsResults[1].total").value(2))
                .andExpect(jsonPath("$.questionsResults[1].answersResults[0].total").value(0))
                .andExpect(jsonPath("$.questionsResults[1].answersResults[1].total").value(2));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldGetUsersSurveyInfos() throws Exception {
        //given
        var user = addUser();
        var survey1 = createAndSaveSurvey(user);
        var survey2 = createAndSaveSurvey(user);

        //when
        var result = mockMvc.perform(get("/api/surveys/user/{username}", USERNAME)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].surveyId").value(survey1.getId()))
                .andExpect(jsonPath("$.content[1].surveyId").value(survey2.getId()));
    }

    private User addUser() {
        var user = User.builder()
                .username(USERNAME)
                .password("pwd")
                .build();
        return userRepository.save(user);
    }

    private SurveyDto createSurveyDto() {
        return SurveyDto.builder()
                .description("Description")
                .questions(List.of(
                        SurveyQuestionDto.builder()
                                .text("Question 2")
                                .order(2)
                                .type(SurveyQuestionType.SINGLE_CHOICE)
                                .answers(List.of(
                                        SurveyQuestionAnswerDto.builder()
                                                .text("Answer 1")
                                                .order(1)
                                                .build(),
                                        SurveyQuestionAnswerDto.builder()
                                                .text("Answer 2")
                                                .order(2)
                                                .build()
                                ))
                                .build(),
                        SurveyQuestionDto.builder()
                                .text("Question 1")
                                .order(1)
                                .type(SurveyQuestionType.MULTI_CHOICE)
                                .answers(List.of(
                                        SurveyQuestionAnswerDto.builder()
                                                .text("Answer 1")
                                                .order(1)
                                                .build(),
                                        SurveyQuestionAnswerDto.builder()
                                                .text("Answer 2")
                                                .order(2)
                                                .build()
                                ))
                                .build()
                ))
                .configuration(
                        SurveyConfigurationDto.builder()
                                .isActive(true)
                                .startDate(OffsetDateTime.now().minusDays(1))
                                .endDate(OffsetDateTime.now().plusDays(1))
                                .exclusions(
                                        List.of(
                                                SurveyQuestionExclusionDto
                                                        .builder()
                                                        .questionOrder(1L)
                                                        .answerOrder(1L)
                                                        .excludedQuestionOrder(2L)
                                                        .build()))
                                .build())
                .build();
    }

    private Survey createAndSaveSurvey(User user) {
        var survey = Survey.builder()
                .description("Description")
                .owner(user)
                .questions(List.of(
                        SurveyQuestion.builder()
                                .text("Question 2")
                                .order(2)
                                .type(SurveyQuestionType.SINGLE_CHOICE)
                                .answers(List.of(
                                        SurveyQuestionAnswer.builder()
                                                .text("Answer 1")
                                                .order(1)
                                                .build(),
                                        SurveyQuestionAnswer.builder()
                                                .text("Answer 2")
                                                .order(2)
                                                .build()
                                ))
                                .build(),
                        SurveyQuestion.builder()
                                .text("Question 1")
                                .order(1)
                                .type(SurveyQuestionType.MULTI_CHOICE)
                                .answers(List.of(
                                        SurveyQuestionAnswer.builder()
                                                .text("Answer 1")
                                                .order(1)
                                                .build(),
                                        SurveyQuestionAnswer.builder()
                                                .text("Answer 2")
                                                .order(2)
                                                .build()
                                ))
                                .build()
                ))
                .configuration(
                        SurveyConfiguration.builder()
                                .isActive(true)
                                .startDate(OffsetDateTime.now().minusDays(1))
                                .endDate(OffsetDateTime.now().plusDays(1))
                                .exclusions(
                                        List.of(
                                                SurveyQuestionExclusion
                                                        .builder()
                                                        .questionOrder(1)
                                                        .answerOrder(1)
                                                        .excludedQuestionOrder(2)
                                                        .build()))
                                .build())
                .build();

        return surveyRepository.save(survey);
    }

    private CompletedSurveyDto createCompletedSurveyDto(Survey survey) {
        return CompletedSurveyDto.builder()
                .surveyId(survey.getId())
                .completedQuestions(List.of(
                        CompletedSurveyQuestionDto.builder()
                                .questionId(survey.getQuestions().get(0).getId())
                                .questionAnswerIds(Set.of(survey.getQuestions().get(0).getAnswers().get(0).getId()))
                                .build(),
                        CompletedSurveyQuestionDto.builder()
                                .questionId(survey.getQuestions().get(1).getId())
                                .questionAnswerIds(Set.of(survey.getQuestions().get(1).getAnswers().get(1).getId()))
                                .build()
                ))
                .build();
    }

    private CompletedSurvey createAndSaveCompletedSurvey(Survey survey) {
        var completedSurvey = CompletedSurvey.builder()
                .survey(survey)
                .completedQuestions(List.of(
                        CompletedQuestion.builder()
                                .question(survey.getQuestions().get(0))
                                .answers(List.of(survey.getQuestions().get(0).getAnswers().get(0)))
                                .build(),
                        CompletedQuestion.builder()
                                .question(survey.getQuestions().get(1))
                                .answers(List.of(survey.getQuestions().get(1).getAnswers().get(1)))
                                .build()
                ))
                .build();

        return completedSurveyRepository.save(completedSurvey);
    }

    private void cleanDb() {
        completedSurveyRepository.deleteAll();
        surveyRepository.deleteAll();
        userRepository.deleteAll();
    }
}