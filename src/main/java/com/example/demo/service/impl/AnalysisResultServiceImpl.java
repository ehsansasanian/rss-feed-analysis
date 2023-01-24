package com.example.demo.service.impl;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.AnalysisResult;
import com.example.demo.repo.AnalysisResultRepository;
import com.example.demo.service.AnalysisResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisResultServiceImpl implements AnalysisResultService {
    private final AnalysisResultRepository analysisResultRepository;

    @Override
    public List<AnalysisResult> findHighFrequentCommonItems(AnalysisRequest analysisRequest) {
        return analysisResultRepository
                .findByAnalysisRequestOrderByFrequencyDesc(analysisRequest, PageRequest.of(0, 3));
    }
}
