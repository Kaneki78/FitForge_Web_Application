package com.fitforge.FitForge.dto;

import java.util.List;

public class ChartDataDto {

    private List<String> labels; // e.g., ["Mon", "Tue", "Wed"]
    private List<Double> data;   // e.g., [75.5, 75.2, 75.0]

    public ChartDataDto(List<String> labels, List<Double> data) {
        this.labels = labels;
        this.data = data;
    }

    // Getters and Setters
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
}