package com.pollite.model.survey;

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
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "survey_configuration")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isActive;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionExclusion> exclusions;
}
