package com.pollite.pollite.controller;

import com.pollite.pollite.dto.CompletedSurveyDto;
import com.pollite.pollite.dto.SurveyDto;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.service.survey.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createSurvey(@Valid @RequestBody SurveyDto surveyDto, Principal principal)
            throws UserDoesNotExistException {
        surveyService.addSurvey(surveyDto, principal);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable Long surveyId) {
        return surveyService.findSurvey(surveyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/submit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void submitCompletedSurvey(@Valid @RequestBody CompletedSurveyDto completedSurveyDto)
            throws SurveyDoesNotExistException, InvalidCompletedSurveyException, SurveyNotActiveException {
        surveyService.submitCompletedSurvey(completedSurveyDto);
    }
}
