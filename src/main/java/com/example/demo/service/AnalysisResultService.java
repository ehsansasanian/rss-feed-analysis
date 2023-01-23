package com.example.demo.service;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.AnalysisResult;

import java.util.List;

public interface AnalysisResultService {
    List<AnalysisResult> findHighFrequentCommonItems(AnalysisRequest analysisRequest);
}
