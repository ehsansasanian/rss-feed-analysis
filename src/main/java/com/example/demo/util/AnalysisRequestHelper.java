package com.example.demo.util;

import com.example.demo.domain.AnalysisRequest;
import com.example.demo.domain.AnalysisResult;
import com.example.demo.domain.AnalysisResultDetail;
import com.example.demo.domain.Item;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnalysisRequestHelper {
    private final Map<String, FrequencyDetails> analyzedResult = new HashMap<>();

    /**
     * All the words of each feed are accumulated within a HashSet (each word is individual)
     * Therefore, if it already exists with the hashMap, then we know we found another match which is from a different feed.
     * As result, the frequency should be incremented. However, If it doesn't exit in the hashmap, it must be added.
     *
     * @param word:   is the key of the HashMap
     * @param underProcess: is the related item of the source feed
     * @param target: is the related item of the feed that we searched inside
     */
    public void addResult(String word, Item underProcess, Item target) {
        if (analyzedResult.containsKey(word)) {
            analyzedResult.get(word).incrementFrequency(underProcess, target);
        } else {
            analyzedResult.put(word, new FrequencyDetails(underProcess, target));
        }
    }

    public List<AnalysisResultDetail> generateResultDetails(AnalysisRequest analysisRequest) {
        List<AnalysisResultDetail> resultDetails = new ArrayList<>();

        for (Map.Entry<String, FrequencyDetails> entry : analyzedResult.entrySet()) {
            AnalysisResult analysisResult = new AnalysisResult();
            analysisResult.setAnalysisRequest(analysisRequest);
            analysisResult.setWord(entry.getKey());
            analysisResult.setFrequency(entry.getValue().frequency);

            for (Item item : entry.getValue().relatedItems) {
                AnalysisResultDetail resultDetail = new AnalysisResultDetail();
                resultDetail.setAnalysisResult(analysisResult);
                resultDetail.setRelatedItem(item);
                resultDetails.add(resultDetail);
            }
        }
        return resultDetails;
    }

    @Getter
    private static class FrequencyDetails {
        private int frequency = 2;
        private final Set<Item> relatedItems = new HashSet<>();

        public void incrementFrequency(Item underProcess, Item target) {
            frequency++;
            relatedItems.add(underProcess);
            relatedItems.add(target);
        }

        public FrequencyDetails(Item underProcess, Item target) {
            relatedItems.add(underProcess);
            relatedItems.add(target);
        }

        private FrequencyDetails() {
        }
    }

}
