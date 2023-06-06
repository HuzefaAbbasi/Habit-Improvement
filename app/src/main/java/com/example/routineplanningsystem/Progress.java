package com.example.routineplanningsystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class Progress extends Routine{
    private int feeling;
    private int energy;

    public Progress(LocalDate date, LocalTime startTime, LocalTime endTime, Task task, int feeling, int energy) {
        super(date, startTime, endTime, task);
        this.feeling = feeling;
        this.energy = energy;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
