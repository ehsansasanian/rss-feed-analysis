package com.example.demo.service;

import com.example.demo.domain.AnalysisResult;
import com.example.demo.domain.Item;

import java.util.List;

public interface ItemService {

    List<Item> findItems(AnalysisResult analysisResult);
}
