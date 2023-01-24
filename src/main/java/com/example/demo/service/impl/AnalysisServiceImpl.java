package com.example.demo.service.impl;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.Header;
import com.example.demo.domain.Item;
import com.example.demo.exception.AnalysisResultNotFoundException;
import com.example.demo.repo.AnalysisRepository;
import com.example.demo.repo.AnalysisResultDetailRepository;
import com.example.demo.repo.ItemRepository;
import com.example.demo.service.AnalysisResultService;
import com.example.demo.service.AnalysisService;
import com.example.demo.util.AnalysisRequestHelper;
import com.example.demo.util.RSSFeedGateway;
import com.example.demo.util.RSSFeedMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImpl implements AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final AnalysisResultService analysisResultService;
    private final RSSFeedGateway rssFeedGateway;
    private final RSSFeedMapper rssFeedMapper;
    private final ItemRepository itemRepository;
    private final AnalysisResultDetailRepository analysisResultDetailRepository;


    //TODO: STOP WORDS, EFFICIENCY, A BETTER MAPPER, (TREE-SET), REGEX
    @Override
    @Transactional
    @Async("analyzeTaskExecutor")
    public void analyze(AnalysisRequest analysisRequest, List<String> urls) {
        log.info("process started.");
        List<Object> objects = urls.stream()
                .map(this.rssFeedGateway::getFeedAsObject)
                .filter(Objects::nonNull).toList();

        HashMap<Header, List<Item>> hashMap = this.rssFeedMapper.mapAndGetItems(objects);

        AnalysisRequestHelper analysisHelper = new AnalysisRequestHelper();

        Set<Header> processedHeaders = new HashSet<>();
        Header processing;
        int skip = 0;

        log.info("calculating the most frequent words");
        do {
            processing = hashMap.keySet().stream()
                    .skip(skip)
                    .findFirst()
                    .orElse(null);
            List<Item> valueUnderProcess = hashMap.get(processing);
            for (Header header : hashMap.keySet()) {
                if (processedHeaders.contains(header) || (processing != null && processing.equals(header))) continue;
                List<Item> targetItems = hashMap.get(header);

                for (Item underProcess : valueUnderProcess) {
                    for (String separatedWord : underProcess.getSeparatedWords()) {
                        for (Item target : targetItems) {
                            if (target.getSeparatedWords().contains(separatedWord)) {
                                analysisHelper.addResult(separatedWord, underProcess, target);
                            }
                        }
                    }
                }
            }
            processedHeaders.add(processing);
            skip++;
        } while (hashMap.keySet().size() != processedHeaders.size());
        log.info("calculation done.\nsaving the POJOs.");

        itemRepository.saveAll(hashMap.values().stream().flatMap(Collection::stream).toList());
        analysisResultDetailRepository.saveAll(analysisHelper.generateResultDetails(analysisRequest));
        log.info("Objects saved successfully.");
    }

    @Override
    public AnalysisRequest findAnalysisResult(UUID uuid) {
        return analysisRepository.findById(uuid)
                .map(req -> req.setAnalysisResults(analysisResultService.findHighFrequentCommonItems(req)))
                .filter(a -> !a.getAnalysisResults().isEmpty())
                .map(req -> {
                    req.getAnalysisResults()
                            .forEach(r -> r.setAnalysisResultDetails(
                                    analysisResultDetailRepository.findAllByAnalysisResult(r)));
                    return req;
                })
                .orElseThrow(AnalysisResultNotFoundException::new);
    }
}
