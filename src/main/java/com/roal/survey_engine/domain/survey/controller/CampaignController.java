package com.roal.survey_engine.domain.survey.controller;

import com.roal.survey_engine.domain.survey.dto.campaign.CampaignDto;
import com.roal.survey_engine.domain.survey.dto.campaign.CreateCampaignDto;
import com.roal.survey_engine.domain.survey.dto.survey.SurveyListElementDto;
import com.roal.survey_engine.domain.survey.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Tag(name = "Campaign", description = "Campaign API")
@RequestMapping("/api/")
@RestController
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Operation(summary = "Find Campaign by ID")
    @GetMapping(value = "campaigns/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto getById(@PathVariable @NotBlank String id) {
        return campaignService.findCampaignDtoById(id);
    }

    @Operation(summary = "Create new Campaign")
    @PostMapping(value = "/surveys/{surveyId}/campaigns", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CampaignDto createCampaign(@NotNull @Valid @RequestBody CreateCampaignDto campaignDto,
                               @PathVariable String surveyId) {
        return campaignService.create(campaignDto, surveyId);
    }

    @Operation(summary = "Update Campaign by ID")
    @PutMapping(value = "campaigns/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    CampaignDto put(@NotNull @Valid @RequestBody CampaignDto campaignDto,
                    @PathVariable @NotBlank String id) {
        return campaignService.update(campaignDto, id);
    }

    @Operation(summary = "Delete Campaign by ID")
    @DeleteMapping("campaigns/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable @NotBlank String id) {
        campaignService.deleteCampaignById(id);
    }

    @Operation(summary = "Find all campaigns")
    @GetMapping(value = "/campaigns", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SurveyListElementDto> getPublicSurveys(Pageable pageable) {
        return campaignService.findPublicCampaigns(pageable);
    }
}
