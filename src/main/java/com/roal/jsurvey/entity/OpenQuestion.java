package com.roal.jsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OpenQuestion extends AbstractSurveyElement implements SurveyElement{


    private String question;
    private int position;

    public OpenQuestion() {
    }

    public OpenQuestion(String question) {
        this.question = question;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public String getQuestion() {
        return question;
    }
}
