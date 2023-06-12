package com.example.routineplanningsystem;

import java.time.LocalDate;
import java.time.LocalTime;


public class Routine {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Task task;

    public Routine(LocalDate date, LocalTime startTime, LocalTime endTime, Task task) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
    }

    // public void insert(){};
    // public void update(){};
    public void deleteRoutine(Task task, LocalDate localDate, LocalTime localTime){

    };

    void insertRoutine(Task task, LocalDate localDate, LocalTime localTime, int enumEnergy, int enumFeeling) {

    }

    void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime, int enumEnergy, int enumFeeling) {

    }


    void insertRoutine(Task task, LocalDate localDate, LocalTime localTime) {

    }




    void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime) {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", task=" + task +
                '}';
    }
}

