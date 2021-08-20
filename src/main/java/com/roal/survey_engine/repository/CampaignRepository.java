package com.roal.survey_engine.repository;

import com.roal.survey_engine.entity.survey.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByHiddenIsFalseAndActiveIsTrue();
}
