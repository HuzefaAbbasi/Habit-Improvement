package com.example.routineplanningsystem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    //  private static DBHelper instance;

    // public static synchronized DBHelper getInstance (Context context) { if (instance == null) { instance = new DBHelper (context.getApplicationContext ()); } return instance; }

    private DBHelper (Context context) { super (context, "db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTaskTable = "Create Table Task " +
                "(name varchar(50) PRIMARY KEY, " +
                "description varchar(2000)," +
                "taskType int NOT NULl)";


        String createScheduleTable = "Create Table Schedule (" +
                "startTime TIME," +
                "endTime TIME," +
                "date DATE," +
                "taskName varchar(50) NOT NULL," +
                "PRIMARY KEY(startTime, endTime ,date)," +
                "FOREIGN KEY(taskName) REFERENCES Task(name)" +
                ");";

        String createProgressTable = "Create Table Progress (" +
                "startTime TIME," +
                "endTime TIME," +
                "date DATE," +
                "taskName varchar(50) NOT NULL," +
                "energy int CONSTRAINT energy CHECK (energy BETWEEN 1 AND 3)," +
                "feeling int CONSTRAINT feeling CHECK (feeling BETWEEN 1 AND 5)," +
                "PRIMARY KEY(startTime, endTime ,date)," +
                "FOREIGN KEY(taskName) REFERENCES Task(name)" +
                ");";



        db.execSQL(createTaskTable);
        db.execSQL(createScheduleTable);
        db.execSQL(createProgressTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
/*
        String drop = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(drop, new String[]{"myUser"});
        onCreate(db);
*/
    }
    //Task Methods
    public boolean insertTask(Task task){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("taskType" , task.getTaskType());
            values.put("name", task.getTaskName());
            values.put("description", task.getTaskDescription());
            long k = db.insert("Task", null,values);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("insert schedule success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            Log.d("insert task Error", e.getMessage());
            return false;
        }
    }

    public boolean deleteTask(String taskName) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String[] whereArgs = {taskName};
            long k = db.delete("Task",
                    "name = ?",
                    whereArgs);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("delete task success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("delete task Error", e.getMessage());
            return false;
        }

    }

    public boolean updateTask(String oldTaskName, Task newTask){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", newTask.getTaskName());
            contentValues.put("description", newTask.getTaskDescription());
            contentValues.put("taskType" , newTask.getTaskType());
            String[] whereArgs = {oldTaskName};
            long k = db.update("Task", contentValues,"name = ?", whereArgs);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("update task", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("update task Error", e.getMessage());
            return false;
        }

    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM Task";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int taskType = cursor.getInt(cursor.getColumnIndex("taskType"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                    Task task = new Task(name, description, taskType);
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("view task Error", "List successfully returned");
            return taskList;
        }
        catch (Exception e){
            Log.d("view task Error", e.getMessage());
            return taskList;
        }
    }

    //For Schedule

    public boolean insertSchedule(Schedule schedule){
        try{
            Log.d("DBHelper", " Build.VERSION_CODES.O Found)");

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = schedule.getDate().format(dateFormatter);
            values.put("date", formattedDate);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = schedule.getStartTime().format(timeFormatter);
            values.put("startTime", formattedTime);

            String formattedEndTime = schedule.getEndTime().format(timeFormatter);
            values.put("endTime", formattedEndTime);

            values.put("taskName", schedule.getTask().getTaskName());


//            values.put("date", String.valueOf(schedule.getDate()));
//            values.put("startTime", String.valueOf(schedule.getStartTime()));
//           // values.put("endTime", String.valueOf(schedule.getEndTime()));
            long k = db.insert("Schedule", null, values);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("insert schedule success", Long.toString(k));
                return true;
            }
            else{
                Log.d("insert schedule in else", Long.toString(k));
                return false;
            }
        }
        catch (Exception e){
            Log.d("insert schedule failed", e.getMessage());
            return false;
        }
    }

    public boolean deleteSchedule(LocalDate date, LocalTime startTime, LocalTime endTime ) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String startTimeStr = startTime.format(timeFormatter);
            String endTimeStr = endTime.format(timeFormatter);


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = date.format(dateFormatter);

            String[] whereArgs = {dateStr,startTimeStr ,endTimeStr };

            long k = db.delete("Schedule",
                    "date = ? AND startTime = ? AND endTime = ?",
                    whereArgs);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("delete Schedule Success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("delete Schedule failed", e.getMessage());
            return false;
        }
    }
    // work needed

    //returns list of schedules for provided date
    public List<Schedule> getAllScheduleForDate(LocalDate date) {
        List<Schedule> scheduleList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(dateFormatter);
            String dateStr = formattedDate;
            Log.d("view schedule3", dateStr);

            String selectQuerySchedule = "SELECT * FROM Schedule WHERE date = ? ";
            Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{dateStr});

            if (cursor.moveToFirst()) {
                do {
                    Log.d("view schedule2", "Cursor Moved");

                    @SuppressLint("Range") String taskNameSchedule = cursor.getString(cursor.getColumnIndex("taskName"));
                    @SuppressLint("Range") LocalDate dateS = LocalDate.parse(cursor.getString(cursor.getColumnIndex("date")));
                    @SuppressLint("Range") LocalTime startTime = LocalTime.parse(cursor.getString(cursor.getColumnIndex("startTime")));
                    @SuppressLint("Range") LocalTime endTime = LocalTime.parse(cursor.getString(cursor.getColumnIndex("endTime")));
                    String selectQueryTask = "SELECT * FROM Task WHERE name = ?";
                    Cursor cursorTask = db.rawQuery(selectQueryTask, new String[]{taskNameSchedule});
                    if(cursorTask.moveToFirst()){
                        @SuppressLint("Range") String taskName = cursorTask.getString(cursorTask.getColumnIndex("name"));
                        @SuppressLint("Range") String taskDescription = cursorTask.getString(cursorTask.getColumnIndex("description"));
                        @SuppressLint("Range") int taskType = cursorTask.getInt(cursorTask.getColumnIndex("taskType"));


                        //Creating task object to pass to schedule as parameter
                        Task task = new Task(taskName, taskDescription,taskType);
                        // why this? Schedule schedule = new Schedule(null ,localDate, localStartTime, localEndTime, task);
                      //  Schedule schedule = new Schedule (localDate, localStartTime, localEndTime, task);
                        Schedule schedule = new Schedule(dateS, startTime, endTime,task);
                 //       Toast.makeText(ScheduleList,dateS.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("view schedule1", dateS.toString() + startTime.toString() + endTime.toString());
                        scheduleList.add(schedule);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("view schedule", "List successfully returned");
            return scheduleList;
        }
        catch (Exception e){
            Log.d("view schedule Error", e.getMessage());
            return scheduleList;
        }
    }


//-------------------------
    // For Progress

    public boolean insertProgress(Progress progress){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = progress.getDate().format(dateFormatter);
            values.put("date", formattedDate);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = progress.getStartTime().format(timeFormatter);
            values.put("startTime", formattedTime);

            String formattedEndTime = progress.getEndTime().format(timeFormatter);
            values.put("endTime", formattedEndTime);

            values.put("taskName", progress.getTask().getTaskName());
            values.put("energy", String.valueOf(progress.getEnergy()));
            values.put("feeling", String.valueOf(progress.getFeeling()));
            long k = db.insert("Progress", null, values);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("insert progress success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("insert progress failed", e.getMessage());
            return false;
        }
    }

    public boolean deleteProgress(LocalDate date, LocalTime startTime, LocalTime endTime) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String startTimeStr = startTime.format(timeFormatter);
            String endTimeStr = endTime.format(timeFormatter);


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateStr = date.format(dateFormatter);

            String[] whereArgs = {dateStr,startTimeStr ,endTimeStr };

            long k = db.delete("Progress",
                    "date = ? AND startTime = ? AND endTime = ?",
                    whereArgs);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("delete Progress Success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("delete Progress failed", e.getMessage());
            return false;
        }
    }

    /*
    public boolean updateProgress(LocalDate date, LocalTime startTime, LocalTime endTime, Progress progress){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("taskName", progress.getTask().getTaskName());
            values.put("date", String.valueOf(progress.getDate()));
            values.put("startTime", String.valueOf(progress.getStartTime()));
            values.put("endTime", String.valueOf(progress.getEndTime()));
            values.put("energy", String.valueOf(progress.getEnergy()));
            values.put("feeling", String.valueOf(progress.getFeeling()));

            String dateStr = String.valueOf(date);
            String startTimeStr = String.valueOf(startTime);
            String endTimeStr = String.valueOf(endTime);
            String[] whereArgs = {dateStr,startTimeStr ,endTimeStr };

            long k = db.update("Progress", values,
                    "name = ? AND Date = ? AND Time = ?",
                    whereArgs);
            //implement this to insure integrity
            if(k!=-1){
                Log.d("update Progress success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("update Progress failed", e.getMessage());
            return false;
        }
    }


     */

    //returns list of schedules for provided date
    public List<Progress> getAllProgressForDate(LocalDate date) {
        List<Progress> progressList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(dateFormatter);
            String dateStr = formattedDate;
            Log.d("DBHelper-AllProgressGet", dateStr);

            String selectQuerySchedule = "SELECT * FROM Progress WHERE date = ?";
            Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{dateStr});

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String taskNameProgress = cursor.getString(cursor.getColumnIndex("taskName"));
                    @SuppressLint("Range") LocalDate dateS = LocalDate.parse(cursor.getString(cursor.getColumnIndex("date")));
                    @SuppressLint("Range") LocalTime startTime = LocalTime.parse(cursor.getString(cursor.getColumnIndex("startTime")));
                    @SuppressLint("Range") LocalTime endTime = LocalTime.parse(cursor.getString(cursor.getColumnIndex("endTime")));
                    @SuppressLint("Range") int energy = cursor.getInt(cursor.getColumnIndex("energy"));
                    @SuppressLint("Range") int feeling = cursor.getInt(cursor.getColumnIndex("feeling"));

                    String selectQueryTask = "SELECT * FROM Task WHERE name = ?";
                    Cursor cursorTask = db.rawQuery(selectQueryTask, new String[]{taskNameProgress});
                    if(cursorTask.moveToFirst()){
                        @SuppressLint("Range") String taskName = cursorTask.getString(cursorTask.getColumnIndex("name"));
                        @SuppressLint("Range") String taskDescription = cursorTask.getString(cursorTask.getColumnIndex("description"));
                        @SuppressLint("Range") int taskType = cursorTask.getInt(cursorTask.getColumnIndex("taskType"));

                        //Creating task object to pass to schedule as parameter
                        Task task = new Task(taskName, taskDescription,taskType);
                        Progress progress = new Progress(dateS, startTime, endTime,task,feeling,energy);
                        progressList.add(progress);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("view progress", "List successfully returned");
            return progressList;
        }
        catch (Exception e){
            Log.d("view progress Error", e.getMessage());
            return progressList;
        }
    }

  /*  public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email" , user.getEmail());
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        long k = db.insert("myUser", null,values);
        Log.d("mytag", Long.toString(k));
        db.close();
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("myUser", null, "email = ? AND password = ?" ,new String[]{email, password},null,null, null);
        if(cursor != null && cursor.moveToFirst()){

            Log.d("Message",   "email " + cursor.getString(0) + "Password " + cursor.getString(1));
            //ta  Log.d("Message2" + )
            return true;
            //displays to me or to some record? to me it seems. Its query for getting after all      Log.d("mytag", cursor.getString(1))

        }
        else
            return false;
}*/
}
