package com.pollite.pollite.model.survey;

import com.pollite.pollite.model.survey.question.CompletedQuestion;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompletedQuestion> completedQuestions = new ArrayList<>();
}
