package com.example.demo.repo;

import com.example.demo.domain.AnalysisResult;
import com.example.demo.domain.AnalysisResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisResultDetailRepository extends JpaRepository<AnalysisResultDetail, Long> {

    List<AnalysisResultDetail> findAllByAnalysisResult(AnalysisResult analysisResult);
}
