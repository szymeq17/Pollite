package com.pollite.pollite.model.survey.question;

import com.pollite.pollite.model.survey.answer.SurveyQuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_question")
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SurveyQuestionType type;

    private String text;

    @Column(name = "question_order")
    private Integer order;

    @OrderBy("order ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionAnswer> answers = new ArrayList<>();

    public SurveyQuestionAnswer getAnswerByOrder(int order) {
        return answers.stream()
                .filter(surveyQuestionAnswer -> surveyQuestionAnswer.getOrder() == order)
                .findFirst()
                .orElse(null);
    }
}
