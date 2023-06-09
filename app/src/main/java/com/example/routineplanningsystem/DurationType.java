package com.example.routineplanningsystem;

public class DurationType {
    private int type;
    private float duration ;


    public DurationType(int type, float duration) {
        this.type = type;
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
    @Override
    public String toString() {
        return "DurationType{" +
                "type=" + type +
                ", duration=" + duration +
                '}';
    }
}
