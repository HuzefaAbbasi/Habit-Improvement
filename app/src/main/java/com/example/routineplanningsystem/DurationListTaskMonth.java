package com.example.routineplanningsystem;

public class DurationListTaskMonth {
    int month;
    float duration;

    public DurationListTaskMonth(int month, float duration) {
        this.month = month;
        this.duration = duration;
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
}
