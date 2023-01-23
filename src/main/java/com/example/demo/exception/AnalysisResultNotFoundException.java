package com.example.demo.exception;

public class AnalysisResultNotFoundException extends RuntimeException {
    public AnalysisResultNotFoundException() {
        super("Result for the analysis with given UUID does not exist.");
    }
}
