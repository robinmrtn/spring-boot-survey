package com.roal.survey_engine.response.dto.mapping;

import com.roal.survey_engine.response.dto.ElementResponseDto;
import com.roal.survey_engine.response.dto.OpenNumericQuestionResponseDto;
import com.roal.survey_engine.response.entity.OpenNumericQuestionResponse;
import com.roal.survey_engine.survey.entity.Survey;
import com.roal.survey_engine.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.survey.entity.question.OpenNumericQuestion;

public class OpenNumericQuestionResponseMappingStrategy implements ResponseDtoMappingStrategy {

    private OpenNumericQuestionResponse getOpenNumericQuestionResponse(Survey survey, OpenNumericQuestionResponseDto dto) {
        var elementResponse = new OpenNumericQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, dto.getId());

        elementResponse.setAnswer(dto.getValue());
        elementResponse.setOpenNumericQuestion((OpenNumericQuestion) surveyElement);

        return elementResponse;
    }

    @Override
    public OpenNumericQuestionResponse map(Survey survey, ElementResponseDto dto) {
        return getOpenNumericQuestionResponse(survey, (OpenNumericQuestionResponseDto) dto);
    }
}
