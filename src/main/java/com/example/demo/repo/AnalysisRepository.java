package com.example.demo.repo;

import com.example.demo.domain.AnalysisRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalysisRepository extends JpaRepository<AnalysisRequest, UUID> {
}
