package com.example.demo.repo;

import com.example.demo.domain.AnalysisResult;
import com.example.demo.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByAnalysisResult(AnalysisResult analysisResult);
}
