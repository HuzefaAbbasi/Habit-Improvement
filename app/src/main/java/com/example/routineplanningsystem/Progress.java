package com.example.routineplanningsystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class Progress extends Routine {

    private int energy;
    private int feeling;

    public Progress(LocalDate date, LocalTime startTime, LocalTime endTime, Task task, int energy, int feeling) {
        super(date, startTime, endTime, task);
        this.energy = energy;
        this.feeling = feeling;
    }

    void insertRoutine(Task task, LocalDate localDate, LocalTime localTime, int enuintmEnergy, int enumFeeling) {

    }

    void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime, int enumEnergy, int enumFeeling) {

    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}