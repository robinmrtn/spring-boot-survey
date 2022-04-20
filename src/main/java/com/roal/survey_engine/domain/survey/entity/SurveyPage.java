package com.roal.survey_engine.domain.survey.entity;

import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SurveyPage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_surveypage")
    @SequenceGenerator(name = "seq_surveypage")
    private Long id;

    private int position;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("position")
    private List<AbstractSurveyElement> surveyPageElements = new ArrayList<>();

    public SurveyPage() {
        // needed by Hibernate
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AbstractSurveyElement> getSurveyPageElements() {
        return surveyPageElements;
    }

    public int getPosition() {
        return position;
    }

    public SurveyPage setPosition(int position) {
        this.position = position;
        return this;
    }

    public SurveyPage addSurveyElement(AbstractSurveyElement surveyElement) {

        if (surveyElement.getPosition() <= 0) {
            int position = surveyPageElements.size() + 1;
            surveyElement.setPosition(position);
        }
        surveyPageElements.add(surveyElement);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyPage that = (SurveyPage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
