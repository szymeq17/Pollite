package com.pollite.model.survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survey_question_exclusion")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionExclusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer questionOrder;

    private Integer answerOrder;

    private Integer excludedQuestionOrder;
}
