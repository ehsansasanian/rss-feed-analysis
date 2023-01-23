package com.example.demo.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RSSFeedGateway {
    private final RestTemplate restTemplate;

    public Object getFeedAsObject(String url) {
        if (url.isBlank()) {
            return null;
        }

        ResponseEntity<Object> response;

        try {
            response = restTemplate.getForEntity(url, Object.class);
        } catch (Exception e) {
            log.error("Exception with error: {} occurred for URL: {}", e.getMessage(), url);
            return null;
        }

        return response.getBody();
    }
}
