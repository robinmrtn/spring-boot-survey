package com.roal.jsurvey.service;

import com.roal.jsurvey.entity.survey.Survey;
import com.roal.jsurvey.exception.SurveyNotFoundException;
import com.roal.jsurvey.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey findSurveyById(long i) {
        return surveyRepository.findById(i).orElseThrow(SurveyNotFoundException::new);
    }
}
