package com.example.routineplanningsystem;

import java.time.LocalDate;

public class DurationListTask {
    LocalDate date;
    float duration;

    public DurationListTask(LocalDate date, float duration) {
        this.date = date;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DurationListTask{" +
                "date=" + date +
                ", duration=" + duration +
                '}';
    }
}
