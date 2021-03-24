package com.roal.jsurvey.repository;

import com.roal.jsurvey.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findById(long id);

}
