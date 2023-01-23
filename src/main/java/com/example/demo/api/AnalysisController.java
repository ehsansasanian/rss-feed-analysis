package com.example.demo.api;

import com.example.demo.api.request.AnalyzeRequest;
import com.example.demo.domain.AnalysisRequest;
import com.example.demo.repo.AnalysisRepository;
import com.example.demo.service.AnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/analyse")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
    private final AnalysisRepository analysisRepository;

    @PostMapping
    public ResponseEntity<String> analyse(@Valid @RequestBody AnalyzeRequest request) {
        AnalysisRequest analysisRequest = analysisRepository.save(new AnalysisRequest());
        analysisService.analyze(analysisRequest, request.urls());
        return new ResponseEntity<>(analysisRequest.getUid().toString(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/frequency/{uuid}")
    public ResponseEntity<AnalysisRequest> findAnalysisWithHighFrequencyItems(@PathVariable UUID uuid) {
        return new ResponseEntity<>(analysisService.findAnalysisWithHighFrequencyItems(uuid), HttpStatus.OK);
    }
}
