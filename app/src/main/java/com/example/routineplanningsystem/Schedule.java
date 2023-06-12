package com.example.routineplanningsystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule extends Routine{


    //public Schedule(LocalDate localDate, LocalTime localTime, Task task) {


    public Schedule(LocalDate date, LocalTime startTime, LocalTime endTime, Task task) {
        super(date, startTime, endTime, task);
    }

    void insertRoutine(Task task, LocalDate localDate, LocalTime localTime) {
        //we will call insertSchedule here
    }



    void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime) {
        //we will call updateSchedule here

    }





}
