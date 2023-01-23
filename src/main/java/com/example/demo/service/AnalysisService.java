package com.example.demo.service;

import com.example.demo.domain.AnalysisRequest;

import java.util.List;
import java.util.UUID;

public interface AnalysisService {
    void analyze(AnalysisRequest analysisRequest, List<String> urls);

    AnalysisRequest findAnalysisWithHighFrequencyItems(UUID uuid);
}
