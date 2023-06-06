package com.example.routineplanningsystem;



import java.time.LocalDate;
import java.time.LocalTime;

public class Routine {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Task task;
    //    private Context context;

//      public void dbHelperCreation(){
//          DBHelper habit = new DBHelper(context,"db",null,1);
//      }

    //DBHelper habit = new DBHelper(context,"db",null,1);
    public Routine( LocalDate date, LocalTime startTime, LocalTime endTime, Task task) {
        //    this.context = context;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
    }

    // public void insert(){};
    // public void update(){};
//        public void deleteSchedule(LocalDate scheduleDate, LocalTime startTime, LocalTime endTime){
//            habit.deleteSchedule(scheduleDate,startTime,endTime);
//        };


//        public void insertSchedule(Task task, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
//               Schedule schedule = new Schedule(context.getApplicationContext(), scheduleDate,  startTime,  endTime, task);
//                habit.insertSchedule(schedule);
//        }

//        public void getAllScheduleForDate(LocalDate date){
//            habit.getAllScheduleForDate(date);
//        }

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
}


