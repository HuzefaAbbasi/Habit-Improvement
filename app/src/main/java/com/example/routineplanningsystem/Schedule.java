package com.example.routineplanningsystem;


import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule extends Routine {

    public Schedule(LocalDate date, LocalTime startTime, LocalTime endTime, Task task) {
        super(date, startTime, endTime, task);
    }
}


