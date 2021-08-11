package com.roal.survey_engine.service;

import com.roal.survey_engine.dto.response.ElementResponseDto;
import com.roal.survey_engine.dto.response.OpenQuestionResponseDto;
import com.roal.survey_engine.dto.response.SurveyResponseDto;
import com.roal.survey_engine.entity.survey.Campaign;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.exception.SurveyNotFoundException;
import com.roal.survey_engine.repository.CampaignRepository;
import com.roal.survey_engine.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    @MockBean
    private CampaignRepository campaignRepository;

    @Test
    @DisplayName("Test findById success")
    void testFindByIdSuccess() {
        var survey = new Survey("This is a Survey");

        given(surveyRepository.findById(2)).willReturn(Optional.of(survey));

        Survey returnedSurvey = surveyService.findSurveyById(2);

        assertSame(survey, returnedSurvey);

    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        var survey = Optional.of(new Survey("This is a Survey"));

        given(surveyRepository.findById(3)).willReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> surveyService.findSurveyById(3));
    }

    @Test
    @DisplayName("test insert Survey Response DTO")
    void testInsertSurveyResponseDto() {
        var surveyResponseDto = new SurveyResponseDto();
        List<ElementResponseDto> elementResponseDtos = new ArrayList<>();
        elementResponseDtos.add(new OpenQuestionResponseDto(9, "This is an answer"));
        surveyResponseDto.setElementResponseDtos(elementResponseDtos);
    }

    @Test
    void testGetPublicAndActiveSurveys() {
        given(campaignRepository.findByHiddenIsFalseAndActiveIsTrue()).willReturn(
                List.of(new Campaign().setSurvey(new Survey()),
                        new Campaign().setSurvey(new Survey()))
        );
        assertEquals(2, surveyService.getPublicAndActiveSurveys().size());
    }


}
