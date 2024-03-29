package com.pollite.service.survey;

import com.pollite.dto.SurveyDto;
import com.pollite.dto.SurveyInfoDto;
import com.pollite.dto.mapper.SurveyMapper;
import com.pollite.exception.SurveyDoesNotExistException;
import com.pollite.exception.UserDoesNotExistException;
import com.pollite.exception.UserNotAuthorizedException;
import com.pollite.model.survey.Survey;
import com.pollite.model.survey.SurveyConfiguration;
import com.pollite.repository.CompletedSurveyRepository;
import com.pollite.repository.SurveyRepository;
import com.pollite.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.Clock;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SurveyService {

    private final UserService userService;

    private final SurveyRepository surveyRepository;
    private final CompletedSurveyRepository completedSurveyRepository;
    private final SurveyMapper surveyMapper;


    private final Clock clok;

    public Long addSurvey(SurveyDto surveyDto, Principal principal) throws UserDoesNotExistException {
        var owner = userService.findUserByUsername(principal.getName());
        var survey = surveyMapper.fromDto(surveyDto);
        survey.setOwner(owner);

        if (survey.getConfiguration() == null) {
            survey.setConfiguration(createDefaultConfiguration());
        }

        var createdSurvey = surveyRepository.save(survey);
        return createdSurvey.getId();
    }

    @Transactional
    public void deleteSurvey(Long surveyId, Principal principal) {
        var username = principal.getName();
        var survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyDoesNotExistException(surveyId));

        if (!survey.getOwner().getUsername().equals(username)) {
            throw new UserNotAuthorizedException(username);
        }

        completedSurveyRepository.deleteAllBySurveyId(surveyId);
        surveyRepository.deleteById(surveyId);
    }

    public Optional<SurveyDto> findSurvey(Long id) {
        return surveyRepository.findById(id).map(surveyMapper::toDto);
    }

    public Page<SurveyInfoDto> getUsersSurveyInfos(String username, Pageable pageable, Principal principal) {
        if (!principal.getName().equals(username)) {
            throw new UserNotAuthorizedException(principal.getName());
        }

        return surveyRepository.findAllByOwnerUsername(username, pageable).map(this::toSurveyInfoDto);
    }

    public Page<SurveyInfoDto> getAllSurveyInfos(Pageable pageable) {
        return surveyRepository.findAll(pageable).map(this::toSurveyInfoDto);
    }

    private SurveyConfiguration createDefaultConfiguration() {
        return SurveyConfiguration.builder()
                .isActive(true)
                .build();
    }

    private SurveyInfoDto toSurveyInfoDto(Survey survey) {
        return SurveyInfoDto.builder()
                .surveyId(survey.getId())
                .description(survey.getDescription())
                .isActive(survey.getConfiguration().getIsActive())
                .owner(survey.getOwner().getUsername())
                .startDate(survey.getConfiguration().getStartDate())
                .endDate(survey.getConfiguration().getEndDate())
                .build();
    }
}
