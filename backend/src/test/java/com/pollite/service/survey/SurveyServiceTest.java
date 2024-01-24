package com.pollite.service.survey;

import com.pollite.dto.SurveyDto;
import com.pollite.dto.mapper.SurveyMapper;
import com.pollite.model.survey.Survey;
import com.pollite.exception.UserNotAuthorizedException;
import com.pollite.model.auth.User;
import com.pollite.model.survey.SurveyConfiguration;
import com.pollite.repository.SurveyRepository;
import com.pollite.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    private static final String USERNAME = "USERNAME";
    private static final Long SURVEY_ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyMapper surveyMapper;

    @Mock
    private Clock clock;

    @Mock
    private Principal principal;

    @InjectMocks
    private SurveyService sut;

    @Test
    public void shouldAddSurvey() {
        //given
        var surveyDto = SurveyDto.builder().build();
        var survey = Survey.builder().build();
        when(principal.getName()).thenReturn(USERNAME);
        when(userService.findUserByUsername(USERNAME)).thenReturn(mockUser());
        when(surveyMapper.fromDto(surveyDto)).thenReturn(survey);
        survey.setId(SURVEY_ID);
        when(surveyRepository.save(survey)).thenReturn(survey);

        //when
        var result = sut.addSurvey(surveyDto, principal);

        //then
        assertThat(result).isEqualTo(SURVEY_ID);
    }

    @Test
    public void shouldDeleteSurvey() {
        //given
        var user = mockUser();
        var survey = Survey.builder()
                .owner(user)
                .build();
        when(principal.getName()).thenReturn(USERNAME);
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));

        //when
        sut.deleteSurvey(SURVEY_ID, principal);

        //then
        verify(surveyRepository).deleteById(SURVEY_ID);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotAuthorizedToDeleteSurvey() {
        //given
        var user = mockUser();
        var survey = Survey.builder()
                .owner(user)
                .build();
        when(principal.getName()).thenReturn("differentUsername");
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));

        //then
        assertThrows(UserNotAuthorizedException.class, () -> sut.deleteSurvey(SURVEY_ID, principal));
    }

    @Test
    public void shouldFindSurvey() {
        //given
        var surveyDto = SurveyDto.builder().build();
        var survey = Survey.builder().build();
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(survey));
        when(surveyMapper.toDto(survey)).thenReturn(surveyDto);

        ///when
        var result = sut.findSurvey(SURVEY_ID);

        //then
        assertThat(result.get()).isEqualTo(surveyDto);
    }

    @Test
    public void shouldGetUsersSurveyInfos() {
        //given
        var survey = Survey.builder()
                .id(SURVEY_ID)
                .description("description")
                .configuration(
                        SurveyConfiguration.builder()
                                .isActive(true)
                                .startDate(OffsetDateTime.MIN)
                                .endDate(OffsetDateTime.MAX)
                                .build())
                .owner(User.builder()
                        .username(USERNAME)
                        .build())
                .build();
        var pageableMock = mock(Pageable.class);
        var surveyPage = new PageImpl<>(Collections.singletonList(survey));

        when(principal.getName()).thenReturn(USERNAME);
        when(surveyRepository.findAllByOwnerUsername(USERNAME, pageableMock)).thenReturn(surveyPage);

        //when
        var result = sut.getUsersSurveyInfos(USERNAME, pageableMock, principal);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1L);

        var surveyInfoDto = result.get().findFirst().get();
        assertThat(surveyInfoDto.getSurveyId()).isEqualTo(SURVEY_ID);
        assertThat(surveyInfoDto.getDescription()).isEqualTo(survey.getDescription());
        assertThat(surveyInfoDto.getStartDate()).isEqualTo(OffsetDateTime.MIN);
        assertThat(surveyInfoDto.getEndDate()).isEqualTo(OffsetDateTime.MAX);
        assertThat(surveyInfoDto.getOwner()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldGetAllSurveyInfos() {
        //given
        var survey = Survey.builder()
                .id(SURVEY_ID)
                .description("description")
                .configuration(
                        SurveyConfiguration.builder()
                                .isActive(true)
                                .startDate(OffsetDateTime.MIN)
                                .endDate(OffsetDateTime.MAX)
                                .build())
                .owner(User.builder()
                        .username(USERNAME)
                        .build())
                .build();
        var pageableMock = mock(Pageable.class);
        var surveyPage = new PageImpl<>(Collections.singletonList(survey));

        when(surveyRepository.findAll(pageableMock)).thenReturn(surveyPage);

        //when
        var result = sut.getAllSurveyInfos(pageableMock);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1L);

        var surveyInfoDto = result.get().findFirst().get();
        assertThat(surveyInfoDto.getSurveyId()).isEqualTo(SURVEY_ID);
        assertThat(surveyInfoDto.getDescription()).isEqualTo(survey.getDescription());
        assertThat(surveyInfoDto.getStartDate()).isEqualTo(OffsetDateTime.MIN);
        assertThat(surveyInfoDto.getEndDate()).isEqualTo(OffsetDateTime.MAX);
        assertThat(surveyInfoDto.getOwner()).isEqualTo(USERNAME);
    }

    private User mockUser() {
        return User.builder()
                .username(USERNAME)
                .build();
    }

}