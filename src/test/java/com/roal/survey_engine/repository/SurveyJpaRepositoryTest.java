package com.roal.survey_engine.repository;

import com.roal.survey_engine.entity.question.ClosedQuestion;
import com.roal.survey_engine.entity.question.OpenTextQuestion;
import com.roal.survey_engine.entity.survey.Survey;
import com.roal.survey_engine.entity.survey.SurveyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class SurveyJpaRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("should persist and return survey when survey is saved in repository")
    void testInsertSingleSurvey() {
        Survey survey = createSurvey();

        surveyRepository.save(survey);

        flushRepository();

        var receivedSurvey = surveyRepository.findById(survey.getId());

        assertAll(() -> assertTrue(receivedSurvey.isPresent()),
                () -> assertEquals(1, receivedSurvey.get().getSurveyPages().size()),
                () -> assertEquals(1, receivedSurvey.get().getSurveyPages().get(0)
                        .getSurveyPageElements().size()),
                () -> assertEquals(survey, receivedSurvey.get()),
                () -> assertNotSame(survey, receivedSurvey.get()));
    }

    @Test
    @DisplayName("should remove Survey from repository when deleteById is called")
    void shouldRemoveSurveyFromRepositoryWhenSurveyIsDeleted() {
        Survey survey = createSurvey();

        surveyRepository.save(survey);

        flushRepository();

        var receivedSurvey = surveyRepository.findById(survey.getId());

        surveyRepository.deleteById(survey.getId());

        assertEquals(0, surveyRepository.findAll().size());
    }

    private void flushRepository() {
        testEntityManager.flush();
        // clears persistence context
        // all entities are now detached and can be fetched again
        testEntityManager.clear();
    }

    private Survey createSurvey() {

        var openQuestion = new OpenTextQuestion("This is an open question?");
        var closedQuestion = new ClosedQuestion("This is a closed question?");

        var firstSurveyPage = new SurveyPage()
                .addSurveyElement(openQuestion)
                .addSurveyElement(closedQuestion);

        return new Survey("This is a Survey")
                .addSurveyPage(firstSurveyPage);
    }

}
