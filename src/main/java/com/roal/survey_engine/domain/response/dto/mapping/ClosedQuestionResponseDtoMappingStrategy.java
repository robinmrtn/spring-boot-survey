package com.roal.survey_engine.domain.response.dto.mapping;

import com.roal.survey_engine.domain.response.dto.ClosedQuestionResponseDto;
import com.roal.survey_engine.domain.response.dto.ElementResponseDto;
import com.roal.survey_engine.domain.response.entity.ClosedQuestionResponse;
import com.roal.survey_engine.domain.survey.entity.Survey;
import com.roal.survey_engine.domain.survey.entity.question.AbstractSurveyElement;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestion;
import com.roal.survey_engine.domain.survey.entity.question.ClosedQuestionAnswer;

import java.util.List;
import java.util.Set;

public class ClosedQuestionResponseDtoMappingStrategy implements ResponseDtoMappingStrategy {
    @Override
    public ClosedQuestionResponse map(Survey survey, ElementResponseDto dto) {
        return getClosedQuestionResponse(survey, (ClosedQuestionResponseDto) dto);
    }

    private ClosedQuestionResponse getClosedQuestionResponse(Survey survey,
                                                             ClosedQuestionResponseDto elementResponseDto) {

        var elementResponse = new ClosedQuestionResponse();
        AbstractSurveyElement surveyElement =
                findSurveyElementById(survey, elementResponseDto.getElementId());

        List<ClosedQuestionAnswer> closedQuestionAnswers =
                getClosedQuestionAnswers(survey, elementResponseDto);

        elementResponse.setAnswers(closedQuestionAnswers);
        elementResponse.setClosedQuestion((ClosedQuestion) surveyElement);

        return elementResponse;
    }

    private List<ClosedQuestionAnswer> getClosedQuestionAnswers(Survey survey,
                                                                ClosedQuestionResponseDto elementResponseDto) {
        Set<Long> answerIds = elementResponseDto.getAnswers();

        return survey.getSurveyPages()
            .stream()
            .flatMap(surveyPage -> surveyPage.getSurveyPageElements().stream())
            .filter(ClosedQuestion.class::isInstance)
            .filter(e -> e.getId() == elementResponseDto.getElementId())
            .flatMap(e -> ((ClosedQuestion) e).getAnswers().stream())
            .filter(closedQuestionAnswer -> answerIds.contains(closedQuestionAnswer.getId()))
            .toList();
    }


}
