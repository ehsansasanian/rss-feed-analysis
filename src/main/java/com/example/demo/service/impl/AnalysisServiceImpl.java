package com.example.demo.service.impl;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.exception.AnalysisResultNotFoundException;
import com.example.demo.repo.AnalysisRepository;
import com.example.demo.service.AnalysisResultService;
import com.example.demo.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImpl implements AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final AnalysisResultService analysisResultService;


    //TODO: STOP WORDS, EFFICIENCY, A BETTER MAPPER, (TREE-SET)
    @Override
    @Transactional
    @Async("analyzeTaskExecutor")
    public void analyze(AnalysisRequest analysisRequest, List<String> urls) {

    }

    @Override
    public AnalysisRequest findAnalysisWithHighFrequencyItems(UUID uuid) {
        return analysisRepository.findById(uuid)
                .map(analysisResult -> analysisResult.setAnalysisResults(
                        analysisResultService.findHighFrequentCommonItems(analysisResult))
                )
                .orElseThrow(AnalysisResultNotFoundException::new);
    }
}


