package com.pollite.model.survey;

import com.pollite.model.survey.question.CompletedQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "completed_survey")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Survey survey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompletedQuestion> completedQuestions = new ArrayList<>();

    public CompletedQuestion getCompletedQuestionById(Long id) {
        return completedQuestions.stream()
                .filter(completedQuestion -> completedQuestion.getQuestion().getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
