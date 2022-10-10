package com.pollite.pollite.model.survey.question;

import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_single_choice_question")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveySingleChoiceQuestion extends SurveyQuestion {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionAnswer> answers = new ArrayList<>();
}
