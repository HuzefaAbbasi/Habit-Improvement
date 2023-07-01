package com.example.routineplanningsystem;

import java.time.LocalDate;

public class RecommendationCalculation {
    LocalDate date;
    int value;

    public RecommendationCalculation(LocalDate date, int value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RecommendationCalculation{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }
}
