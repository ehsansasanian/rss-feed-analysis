package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "analysis_results")
@Data
public class AnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /*
     * In each item there might several in common words. e.g: attack, military
     * */
    @Column(name = "common_words")
    private String commonWords;

    private Integer frequency;

    @ManyToOne
    @JoinColumn(name = "request_analysis_id")
    @JsonIgnore
    private AnalysisRequest analysisRequest;

    private transient List<Item> items;

    public AnalysisResult setItems(List<Item> items) {
        this.items = items;
        return this;
    }
}
