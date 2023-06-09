package com.example.routineplanningsystem;
import java.time.LocalDate;
import java.util.List;

public class DurationListMonth {
    int month;
    List<DurationType> durationTypeList;

    public DurationListMonth(int month, List<DurationType> durationTypeList) {
        this.month = month;
        this.durationTypeList = durationTypeList;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public List<DurationType> getDurationList() {
        return durationTypeList;
    }

    public void setDurationList(List<DurationType> durationTypeList) {
        this.durationTypeList = durationTypeList;
    }
    @Override
    public String toString() {
        return "DurationListMonth{" +
                "month=" + month +
                ", durationTypeList=" + durationTypeList +
                '}';
    }
}
