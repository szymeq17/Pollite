package com.pollite.pollite.service.survey;

import com.pollite.pollite.dto.SurveyResultsDto;
import com.pollite.pollite.exception.SurveyDoesNotExistException;
import com.pollite.pollite.projection.SurveyResultsProjection;
import com.pollite.pollite.repository.CompletedSurveyRepository;
import com.pollite.pollite.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class CompletedSurveyService {

    private final CompletedSurveyRepository completedSurveyRepository;
    private final SurveyRepository surveyRepository;

//    public SurveyResultsDto getSurveyResults(Long surveyId) throws SurveyDoesNotExistException {
//        if (!surveyRepository.existsById(surveyId)) {
//            throw new SurveyDoesNotExistException(surveyId);
//        }
//
//        var resultsProjection = completedSurveyRepository.countAnswers(surveyId);
//
//        Map<Long , Map<Long, List<SurveyResultsProjection>> result = resultsProjection.stream()
//                .collect(
//                        groupingBy(
//                                SurveyResultsProjection::getQuestionId,
//                                groupingBy(SurveyResultsProjection::getAnswerId)))
//    }
}
