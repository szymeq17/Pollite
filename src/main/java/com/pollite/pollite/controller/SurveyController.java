package com.pollite.pollite.controller;

import com.pollite.pollite.dto.CompletedSurveyDto;
import com.pollite.pollite.dto.CompletedSurveyFilter;
import com.pollite.pollite.dto.SurveyDto;
import com.pollite.pollite.dto.SurveyInfoDto;
import com.pollite.pollite.exception.InvalidCompletedSurveyException;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.exception.SurveyNotActiveException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.service.survey.CompletedSurveyService;
import com.pollite.pollite.service.survey.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/survey")
@RequiredArgsConstructor
@CrossOrigin
public class SurveyController {

    private final SurveyService surveyService;
    private final CompletedSurveyService completedSurveyService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long createSurvey(@Valid @RequestBody SurveyDto surveyDto, Principal principal)
            throws UserDoesNotExistException {
        return surveyService.addSurvey(surveyDto, principal);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable Long surveyId) {
        return surveyService.findSurvey(surveyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{surveyId}")
    public void deleteSurvey(@PathVariable Long surveyId, Principal principal) {
        surveyService.deleteSurvey(surveyId, principal);
    }

    @PostMapping("/submit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void submitCompletedSurvey(@Valid @RequestBody CompletedSurveyDto completedSurveyDto)
            throws SurveyDoesNotExistException, InvalidCompletedSurveyException, SurveyNotActiveException {
        completedSurveyService.submitCompletedSurvey(completedSurveyDto);
    }

    @PostMapping("/{surveyId}/results")
    public ResponseEntity<?> getSurveyResults(@PathVariable Long surveyId,
                                              @RequestBody(required = false) List<CompletedSurveyFilter> filters)
            throws SurveyDoesNotExistException {
        return ResponseEntity.ok(completedSurveyService.getSurveyResults(surveyId, filters));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<SurveyInfoDto>> getUsersSurveyInfos(@PathVariable String username,
                                                                   Pageable pageable,
                                                                   Principal principal) {
        return ResponseEntity.ok(surveyService.getUsersSurveyInfos(username, pageable, principal));
    }
}
