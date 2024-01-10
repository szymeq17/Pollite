package com.pollite.model.survey.question;

import com.pollite.model.survey.answer.SurveyQuestionAnswer;
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
import java.util.List;

@Entity
@Table(name = "completed_question")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SurveyQuestion question;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionAnswer> answers;
}
