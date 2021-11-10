package com.roal.survey_engine.repository;

import com.roal.survey_engine.survey.entity.Campaign;
import com.roal.survey_engine.survey.repository.CampaignRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class CampaignJpaRepositoryTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setup() {
        var privateCampaign = new Campaign().setHidden(true).setActive(true);
        var publicCampaign = new Campaign().setHidden(false).setActive(true);
        var deactivatedCampaign = new Campaign().setHidden(false).setActive(false);
        campaignRepository.save(privateCampaign);
        campaignRepository.save(publicCampaign);
        campaignRepository.save(deactivatedCampaign);
        campaignRepository.flush();
    }

    @Test
    void testFindPublicAndActive() {
        List<Campaign> campaigns = campaignRepository.findByHiddenIsFalseAndActiveIsTrue();

        Assertions.assertEquals(1, campaigns.size());
    }
}
