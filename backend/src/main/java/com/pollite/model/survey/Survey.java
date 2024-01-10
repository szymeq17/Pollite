package com.pollite.model.survey;

import com.pollite.model.survey.question.SurveyQuestion;
import com.pollite.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_owner")
    private User owner;

    @Column(name = "description")
    private String description;

    @OrderBy("order ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private SurveyConfiguration configuration;

    public SurveyQuestion getQuestionByOrder(int order) {
        return questions.stream()
                .filter(surveyQuestion -> surveyQuestion.getOrder() == order)
                .findFirst()
                .orElse(null);
    }

    public SurveyQuestion getQuestionById(Long id) {
        return questions.stream()
                .filter(surveyQuestion -> surveyQuestion.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

