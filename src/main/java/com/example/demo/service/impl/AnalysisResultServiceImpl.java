package com.example.demo.service.impl;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.AnalysisResult;
import com.example.demo.repo.AnalysisResultRepository;
import com.example.demo.service.AnalysisResultService;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisResultServiceImpl implements AnalysisResultService {
    private final AnalysisResultRepository analysisResultRepository;
    private final ItemService itemService;

    @Override
    public List<AnalysisResult> findHighFrequentCommonItems(AnalysisRequest analysisRequest) {
        return analysisResultRepository
                .findByAnalysisRequestOrderByFrequencyDesc(analysisRequest, PageRequest.of(0, 3))
                .stream()
                .map(commonItem -> commonItem.setItems(itemService.findItems(commonItem)))
                .collect(Collectors.toList());
    }
}
