package com.roal.survey_engine.domain.survey.dto.element;

public record OpenNumericQuestionDto(String question, int position, long id) implements AbstractElementDto {
    @Override
    public String type() {
        return "opnq";
    }
}