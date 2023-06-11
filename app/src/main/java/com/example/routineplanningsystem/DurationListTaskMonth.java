package com.example.routineplanningsystem;

public class DurationListTaskMonth {
    int month;
    float duration;
    int taskType;


    public DurationListTaskMonth(int month, float duration, int taskType) {
        this.month = month;
        this.duration = duration;
        this.taskType = taskType;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DurationListTaskMonth{" +
                "month=" + month +
                ", duration=" + duration +
                '}';
    }
}
