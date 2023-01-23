package com.example.demo.api.request;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AnalyzeRequest(@Size(min = 2) List<String> urls) {
}
