package com.example.demo.repo;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.AnalysisResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    List<AnalysisResult> findByAnalysisRequestOrderByFrequencyDesc(AnalysisRequest analysisRequest, Pageable pageable);
}
