package com.roal.survey_engine.domain.reporting.service;

import com.roal.survey_engine.domain.reporting.dto.out.AbstractElementReportingDto;
import com.roal.survey_engine.domain.reporting.dto.out.CategoricalReportingDto;
import com.roal.survey_engine.domain.reporting.dto.out.CategoricalReportingItemDto;
import com.roal.survey_engine.domain.reporting.dto.out.NumericReportingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public NumericReportingDto getNumericReportingDto(Long elementId, Long campaignId) {


        var parameters = new MapSqlParameterSource()
            .addValue("elementId", elementId)
            .addValue("campaignId", campaignId);

        try {
            NumericReportingDto numericReportingDto = jdbcTemplate.queryForObject("SELECT COUNT(onqr.answer) as count, MIN(onqr.answer) as min, " +
                    "MAX(onqr.answer) as max, AVG(onqr.answer) as avg, STDDEV(answer) as sd, " +
                    "MEDIAN(onqr.answer) as median " +
                    "FROM open_numeric_question_response onqr " +
                    "JOIN SURVEY_PAGE_SURVEY_PAGE_ELEMENTS spspe ON spspe.SURVEY_PAGE_ELEMENTS_ID = onqr.open_numeric_question_id " +
                    "JOIN SURVEY_PAGE sp ON sp.id = spspe.SURVEY_PAGE_ID " +
                    "JOIN SURVEY s ON s.id = sp.SURVEY_ID " +
                    "JOIN CAMPAIGN c on c.SURVEY_ID = s.id and c.id=:campaignId " +
                    "WHERE onqr.open_numeric_question_id=:elementId " +
                    "GROUP BY onqr.open_numeric_question_id",
                parameters,
                (rs, rowNum) -> new NumericReportingDto(elementId,
                    rs.getInt("count"),
                    rs.getDouble("avg"),
                    rs.getDouble("min"),
                    rs.getDouble("max"),
                    rs.getDouble("median"),
                    0,
                    0,
                    rs.getDouble("sd")));
            return numericReportingDto;
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<AbstractElementReportingDto> getReportsByCampaignId(long campaignId) {
        List<AbstractElementReportingDto> reports = new ArrayList<>();

        reports.addAll(getCategoricalReportsByCampaignId(campaignId));
        reports.addAll(getNumericReportsByCampaignId(campaignId));

        if (reports.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return reports;
    }


    private List<CategoricalReportingDto> getCategoricalReportsByCampaignId(long campaignId) {

        String sql = "SELECT cq.ID as element_id, cqa.value as answer, COUNT(cqa.value) as count, COUNT(cq.ID) as count2 " +
            "FROM CLOSED_QUESTION_RESPONSE_ANSWERS cqra " +
            "JOIN CLOSED_QUESTION_ANSWER cqa ON cqa.id = cqra.answers_id " +
            "JOIN CLOSED_QUESTION_RESPONSE cqr ON cqra.CLOSED_QUESTION_RESPONSE_ID = cqr.ID " +
            "JOIN CLOSED_QUESTION cq ON cqr.CLOSED_QUESTION_ID = cq.ID " +
            "JOIN SURVEY_PAGE_SURVEY_PAGE_ELEMENTS spspe ON spspe.SURVEY_PAGE_ELEMENTS_ID = cq.ID " +
            "JOIN SURVEY_PAGE sp ON sp.id = spspe.SURVEY_PAGE_ID " +
            "JOIN SURVEY s ON s.id = sp.SURVEY_ID " +
            "JOIN CAMPAIGN c on c.SURVEY_ID = s.id " +
            "WHERE c.id=:campaignId " +
            "GROUP BY cq.id, cqa.value";

        SqlRowSet rowSet = jdbcTemplate
            .queryForRowSet(sql, new MapSqlParameterSource("campaignId", campaignId));

        long elementId = -1;
        int count = 0;
        List<CategoricalReportingItemDto> items = null;
        List<CategoricalReportingDto> dtos = new ArrayList<>();
        while (rowSet.next()) {

            if (rowSet.getLong("ELEMENT_ID") != elementId) {
                elementId = elementId == -1 ? rowSet.getLong("ELEMENT_ID") : elementId;
                if (items != null) {
                    var dto = new CategoricalReportingDto(elementId, count, items);
                    dtos.add(dto);
                    elementId = rowSet.getLong("ELEMENT_ID");
                    items = new ArrayList<>();
                    count = 0;
                }
            }
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(new CategoricalReportingItemDto(
                rowSet.getString("ANSWER"),
                rowSet.getInt("COUNT"),
                0.0));
            count += rowSet.getInt("COUNT");
        }
        if (items != null) {
            var dto = new CategoricalReportingDto(elementId, count, items);
            dtos.add(dto);
        }
        return dtos;
    }

    private List<NumericReportingDto> getNumericReportsByCampaignId(long campaignId) {
        String sql = "SELECT onqr.open_numeric_question_id as element_id, " +
            "COUNT(onqr.answer) as count, " +
            "MIN(onqr.answer) as min, " +
            "MAX(onqr.answer) as max, " +
            "AVG(onqr.answer) as avg, " +
            "STDDEV(answer) as sd, " +
            "MEDIAN(onqr.answer) as median " +
            "FROM open_numeric_question_response onqr " +
            "JOIN SURVEY_PAGE_SURVEY_PAGE_ELEMENTS spspe ON spspe.SURVEY_PAGE_ELEMENTS_ID = onqr.open_numeric_question_id " +
            "JOIN SURVEY_PAGE sp ON sp.id = spspe.SURVEY_PAGE_ID " +
            "JOIN SURVEY s ON s.id = sp.SURVEY_ID " +
            "JOIN CAMPAIGN c on c.SURVEY_ID = s.id " +
            "WHERE c.id=:campaignId " +
            "GROUP BY onqr.open_numeric_question_id";

        return jdbcTemplate.query(
            sql,
            new MapSqlParameterSource("campaignId", campaignId),
            (rs, rowNum) ->
                new NumericReportingDto(
                    rs.getLong("element_id"),
                    rs.getInt("count"),
                    rs.getDouble("avg"),
                    rs.getDouble("min"),
                    rs.getDouble("max"),
                    rs.getDouble("median"),
                    0,
                    0,
                    rs.getDouble("sd"))
        );
    }


}
