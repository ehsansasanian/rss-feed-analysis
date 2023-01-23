package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.HashSet;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String link;

    private String description;

    private transient HashSet<String> distinctWords = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "analysis_result_id")
    private AnalysisResult analysisResult;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private Header header;

    public Item setHeader(Header header) {
        this.header = header;
        return this;
    }
}