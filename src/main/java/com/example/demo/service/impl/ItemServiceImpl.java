package com.example.demo.service.impl;

import com.example.demo.domain.AnalysisResult;
import com.example.demo.domain.Item;
import com.example.demo.repo.ItemRepository;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    @Override
    public List<Item> findItems(AnalysisResult analysisResult) {
        return itemRepository.findAllByAnalysisResult(analysisResult);
    }
}
