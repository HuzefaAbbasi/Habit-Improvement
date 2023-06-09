package com.example.routineplanningsystem;

import java.time.LocalDate;
import java.util.List;

public class DurationList {
    LocalDate date;
    List<DurationType> durationTypeList;

    public DurationList(LocalDate date, List<DurationType> durationTypeList) {
        this.date = date;
        this.durationTypeList = durationTypeList;
    }

    @Override
    public String toString() {
        return "DurationList{" +
                "date=" + date +
                ", durationTypeList=" + durationTypeList +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<DurationType> getDurationList() {
        return durationTypeList;
    }

    public void setDurationList(List<DurationType> durationTypeList) {
        this.durationTypeList = durationTypeList;
    }
}
