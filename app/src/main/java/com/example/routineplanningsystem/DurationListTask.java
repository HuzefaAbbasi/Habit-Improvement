package com.example.routineplanningsystem;
import java.time.LocalDate;

public class DurationListTask {
    LocalDate date;
    float duration;
    int taskType;


    public DurationListTask(LocalDate date, float duration, int taskType) {
        this.date = date;
        this.duration = duration;
        this.taskType = taskType;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
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
