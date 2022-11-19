package com.pollite.pollite.service.survey;

import com.pollite.pollite.dto.CompletedSurveyDto;
import com.pollite.pollite.dto.SurveyDto;
import com.pollite.pollite.dto.mapper.CompletedSurveyMapper;
import com.pollite.pollite.dto.mapper.SurveyMapper;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.model.survey.Survey;
import com.pollite.pollite.model.survey.SurveyConfiguration;
import com.pollite.pollite.repository.CompletedSurveyRepository;
import com.pollite.pollite.repository.SurveyRepository;
import com.pollite.pollite.service.poll.UserService;
import com.pollite.pollite.validator.CompletedSurveyValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SurveyService {

    private final UserService userService;

    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;


    private final Clock clok;

    public void addSurvey(SurveyDto surveyDto, Principal principal) throws UserDoesNotExistException {
        var owner = userService.findUserByUsername(principal.getName());
        var survey = surveyMapper.fromDto(surveyDto);
        survey.setOwner(owner);

        if (survey.getConfiguration() == null) {
            survey.setConfiguration(createDefaultConfiguration());
        }

        surveyRepository.save(survey);
    }

    public Optional<SurveyDto> findSurvey(Long id) {
        return surveyRepository.findById(id).map(surveyMapper::toDto);
    }

    private SurveyConfiguration createDefaultConfiguration() {
        return SurveyConfiguration.builder()
                .isActive(true)
                .build();
    }
}
