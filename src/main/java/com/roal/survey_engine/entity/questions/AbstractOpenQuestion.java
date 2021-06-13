package com.roal.survey_engine.entity.questions;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractOpenQuestion extends AbstractSurveyElement {

    protected String question;

    public String getQuestion() {
        return question;
    }
}
