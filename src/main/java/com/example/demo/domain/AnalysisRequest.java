package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "analysis_requests")
@Data
public class AnalysisRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    private transient List<AnalysisResult> analysisResults;

    public AnalysisRequest setAnalysisResults(List<AnalysisResult> analysisResults) {
        this.analysisResults = analysisResults;
        return this;
    }
}
