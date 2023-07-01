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

import java.time.Duration;
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

    private DBHelper (Context context) { super (context, "habit", null, 1); }

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
            if (task.getTaskDescription() != null){
                values.put("description", task.getTaskDescription());
            }
            long k = db.insert("Task", null,values);
            db.close();
            //implement this to insure integrity
            if(k!=-1){
                Log.d("insert task success", Long.toString(k));
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
            String selectQuery = "SELECT * FROM Task ORDER BY name";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int taskType = cursor.getInt(cursor.getColumnIndex("taskType"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                    Task task = new Task(name, description, taskType);
                    taskList.add(task);
                    Log.d("All Tasks", "getAllTasks: "+ task);
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

    public List<String> getAllTasksName(){
        List<String> taskList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT name FROM Task";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    taskList.add(name);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            Log.d("view task Success", "List successfully returned");
            return taskList;
        }
        catch (Exception e){
            Log.d("view task Error", e.getMessage());
            return taskList;
        }
    }

    public boolean checkTask(String taskName){
        boolean exists = false;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT name FROM Task where name = ?";

            Cursor cursor = db.rawQuery(selectQuery, new String[]{taskName});
            if (cursor.moveToFirst()) {
                exists = true;
            }
            cursor.close();
            db.close();
            Log.d("view task Success", "List successfully returned");
        }
        catch (Exception e){
            Log.d("view task Error", e.getMessage());
        }
        return exists;
    }

    //For Schedule
    public boolean insertSchedule(Schedule schedule){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            //getting date into format for insertion in db
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = schedule.getDate().format(dateFormatter);
            Log.d("insert date", formattedDate);

            //getting time into format for insertion in db
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedStartTime = schedule.getStartTime().format(timeFormatter);
            String formattedEndTime = schedule.getEndTime().format(timeFormatter);

            values.put("taskName", schedule.getTask().getTaskName());
            values.put("date", formattedDate);
            values.put("startTime", formattedStartTime);
            values.put("endTime",formattedEndTime);
            long k = db.insert("Schedule", null, values);
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
        catch (Exception e){
            Log.d("insert schedule failed", e.getMessage());
            return false;
        }
    }


    public boolean deleteSchedule(LocalDate date, LocalTime startTime, LocalTime endTime ) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedStartTime = startTime.format(timeFormatter);
            String formattedEndTime = endTime.format(timeFormatter);
            String dateStr = String.valueOf(date);
//            String startTimeStr = String.valueOf(startTime);
//            String endTimeStr = String.valueOf(endTime);

            //getting time into format for insertion in db

            Log.d("Check Delete", "deleteSchedule: "+ dateStr+ " " +formattedStartTime + " "+formattedEndTime);
            String[] whereArgs = {dateStr,formattedStartTime ,formattedEndTime };

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
    public boolean updateSchedule(LocalDate date, LocalTime startTime, LocalTime endTime, Schedule schedule){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("taskName", schedule.getTask().getTaskName());
            values.put("date", String.valueOf(schedule.getDate()));
            values.put("startTime", String.valueOf(schedule.getStartTime()));
            values.put("endTime", String.valueOf(schedule.getEndTime()));

            String dateStr = String.valueOf(date);
            String startTimeStr = String.valueOf(startTime);
            String endTimeStr = String.valueOf(endTime);
            String[] whereArgs = {dateStr,startTimeStr ,endTimeStr };

            long k = db.update("Schedule", values,
                    "name = ? AND Date = ? AND Time = ?",
                    whereArgs);
            //implement this to insure integrity
            if(k!=-1){
                Log.d("update Schedule success", Long.toString(k));
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            Log.d("update Schedule failed", e.getMessage());
            return false;
        }
    }



    //returns list of schedules for provided date
    public List<Schedule> getAllScheduleForDate(LocalDate date) {
        List<Schedule> scheduleList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            //getting date into format for insertion in db
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(dateFormatter);
            String dateStr = String.valueOf(formattedDate);

            Log.d("DateStr", dateStr);
            String selectQuerySchedule = "SELECT * FROM Schedule WHERE date = ? ORDER BY startTime";
            Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{dateStr});

            if (cursor.moveToFirst()) {
                do {
                    Log.d("Schedule", "Cursormoved: ");
                    @SuppressLint("Range") String taskNameSchedule = cursor.getString(cursor.getColumnIndex("taskName"));
                    @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    String selectQueryTask = "SELECT * FROM Task WHERE name = ?";
                    Cursor cursorTask = db.rawQuery(selectQueryTask, new String[]{taskNameSchedule});
                    if(cursorTask.moveToFirst()){
                        Log.d("Task", "Cursormoved: ");
                        @SuppressLint("Range") String taskName = cursorTask.getString(cursorTask.getColumnIndex("name"));
                        @SuppressLint("Range") String taskDescription = cursorTask.getString(cursorTask.getColumnIndex("description"));
                        @SuppressLint("Range") int taskType = cursorTask.getInt(cursorTask.getColumnIndex("taskType"));

                        //Converting date and time to LocalDate and LocalTime
                        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate localDate = LocalDate.parse(dateS, formatterDate);

                        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                        LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                        //Creating task object to pass to schedule as parameter
                        Task task = new Task(taskName, taskDescription,taskType);
                        Schedule schedule = new Schedule(localDate, localStartTime, localEndTime, task);
                        scheduleList.add(schedule);
                        Log.d("Schedule entry", "getAllScheduleForDate: "+ schedule);
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


    public boolean checkDate(String tableName, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime){
        boolean check = true;
        SQLiteDatabase db = this.getReadableDatabase();

        //getting date into format for insertion in db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = newDate.format(dateFormatter);

        Log.d("DateStr", formattedDate);
        String selectQuerySchedule = "SELECT startTime, endTime, date FROM "+tableName+" WHERE date = ?";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedDate});

        if (cursor.moveToFirst()) {
            do {
                Log.d("Schedule", "Cursormoved: ");
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime oldStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime oldEndTime = LocalTime.parse(endTime, formatterTime);

                if(newEndTime.isBefore(oldStartTime) || newStartTime.isAfter(oldEndTime));
                else{
                    check = false;
                }
            }while (cursor.moveToNext());
        }
        else {
            check = true;
        }
        return check;
    }

//-------------------------
    // For Progress


    public boolean insertProgress(Progress progress){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            //getting date into format for insertion in db
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = progress.getDate().format(dateFormatter);
            Log.d("insert date", formattedDate);

            //getting time into format for insertion in db
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedStartTime = progress.getStartTime().format(timeFormatter);
            String formattedEndTime = progress.getEndTime().format(timeFormatter);

            values.put("taskName", progress.getTask().getTaskName());
            values.put("date", formattedDate);
            values.put("startTime",formattedStartTime);
            values.put("endTime", formattedEndTime);
            values.put("energy", progress.getEnergy());
            values.put("feeling", progress.getFeeling());
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

            String formattedStartTime = startTime.format(timeFormatter);
            String formattedEndTime = endTime.format(timeFormatter);
            String dateStr = String.valueOf(date);
//            String startTimeStr = String.valueOf(startTime);
//            String endTimeStr = String.valueOf(endTime);

            //getting time into format for insertion in db

            Log.d("Check Delete", "deleteSchedule: "+ dateStr+ " " +formattedStartTime + " "+formattedEndTime);
            String[] whereArgs = {dateStr,formattedStartTime ,formattedEndTime };


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

    //returns list of schedules for provided date
    public List<Progress> getAllProgressForDate(LocalDate date) {
        List<Progress> progressList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            //getting date into format for insertion in db
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(dateFormatter);
            String dateStr = String.valueOf(formattedDate);

            Log.d("DateStr", dateStr);
            String selectQuerySchedule = "SELECT * FROM Progress WHERE date = ?  ORDER BY startTime";
            Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{dateStr});

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String taskNameProgress = cursor.getString(cursor.getColumnIndex("taskName"));
                    @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    @SuppressLint("Range") int energy = cursor.getInt(cursor.getColumnIndex("energy"));
                    @SuppressLint("Range") int feeling = cursor.getInt(cursor.getColumnIndex("feeling"));

                    String selectQueryTask = "SELECT * FROM Task WHERE name = ?";
                    Cursor cursorTask = db.rawQuery(selectQueryTask, new String[]{taskNameProgress});
                    if(cursorTask.moveToFirst()){
                        @SuppressLint("Range") String taskName = cursorTask.getString(cursorTask.getColumnIndex("name"));
                        @SuppressLint("Range") String taskDescription = cursorTask.getString(cursorTask.getColumnIndex("description"));
                        @SuppressLint("Range") int taskType = cursorTask.getInt(cursorTask.getColumnIndex("taskType"));

                        //Converting date and time to LocalDate and LocalTime
                        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate localDate = LocalDate.parse(dateS, formatterDate);

                        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                        LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                        //Creating task object to pass to schedule as parameter
                        Task task = new Task(taskName, taskDescription,taskType);
                        Progress progress = new Progress(localDate, localStartTime, localEndTime, task,energy, feeling);
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


    //Reports
    public List<DurationList> getWeekOrMonthData(String tableName, String timeSpan){
        List<DurationList> listWithDate = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate;

        if (timeSpan.equalsIgnoreCase("Week")){
            // Get the date of the week before
            startDate = currentDate.minusWeeks(1);
        } else if (timeSpan.equalsIgnoreCase("Month")) {
            // Get the date of the month before
            startDate = currentDate.minusMonths(1);
        }
        else{
            startDate = null;
            Log.d("Time Span", "Wrong Time Span entered ");
        }


        //getting dates into format for selection from db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrentDate = currentDate.format(dateFormatter);
        String formattedStartDate = startDate.format(dateFormatter);

        String selectQuerySchedule = "SELECT n.date, n.startTime, n.endTime, t.taskType FROM " + tableName +
                " n join Task t on n.taskName = t.name  WHERE n.date >= ? AND n.date <= ? ORDER BY n.date";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedStartDate, formattedCurrentDate});
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                @SuppressLint("Range") int taskType= cursor.getInt(cursor.getColumnIndex("taskType"));

                //Converting date and time to LocalDate and LocalTime
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateS, formatterDate);

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                float duration = calculateDuration(localStartTime,localEndTime);

                boolean dateExists = false;
                for (DurationList list : listWithDate) {
                    if (list.getDate().isEqual(localDate)) {
                        // Date already exists, update its duration list
                        List<DurationType> durationList = list.getDurationList();
                        boolean typeFound = false;
                        // Loop through the durationList
                        for (DurationType durationType : durationList) {
                            if (durationType.getType()  == taskType) {
                                // Task type already exists, update the duration value
                                durationType.setDuration(durationType.getDuration() + duration);
                                typeFound = true;
                                break;
                            }
                        }
                        if (!typeFound){
                            durationList.add(new DurationType(taskType, duration));
                            break;
                        }
                        list.setDurationList(durationList);
                        dateExists = true;
                        break;
                    }
                }
                // If date does not exist, create a new DurationList and add it to listWithDate
                if (!dateExists) {
                    List<DurationType> newDurationList = new ArrayList<>();
                    newDurationList.add(new DurationType(1, 0));
                    newDurationList.add(new DurationType(2, 0));
                    newDurationList.add(new DurationType(3, 0));
                    newDurationList.add(new DurationType(4, 0));
                    newDurationList.add(new DurationType(5, 0));
                    newDurationList.add(new DurationType(6, 0));

                    for (DurationType durationType : newDurationList) {
                        if (durationType.getType()  == taskType) {
                            // Task type already exists, update the duration value
                            durationType.setDuration(durationType.getDuration() + duration);
                            break;
                        }
                    }

                    listWithDate.add(new DurationList(localDate, newDurationList));
                }
            }while(cursor.moveToNext());
        }

        // Close the cursor and the database connection
        cursor.close();
        db.close();
        Log.d("List", "getWeekOrMonthData: "+ listWithDate);
        return listWithDate;
    }


    public List<DurationListMonth> getYearData(String tableName){
        List<DurationListMonth> listWithMonths = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate;

        // Get the date of the month before
        startDate = currentDate.minusYears(1);

        //getting dates into format for selection from db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrentDate = currentDate.format(dateFormatter);
        String formattedStartDate = startDate.format(dateFormatter);

        String selectQuerySchedule = "SELECT n.date, n.startTime, n.endTime, t.taskType FROM " + tableName +
                " n join Task t on n.taskName = t.name  WHERE n.date >= ? AND n.date <= ? ORDER BY n.date";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedStartDate, formattedCurrentDate});
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                @SuppressLint("Range") int taskType= cursor.getInt(cursor.getColumnIndex("taskType"));

                //Converting date and time to LocalDate and LocalTime
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateS, formatterDate);
                int monthValue = localDate.getMonthValue();

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                float duration = calculateDuration(localStartTime,localEndTime);
                Log.d("Duration", "getYearData:"+ duration);

                boolean monthExists = false;
                for (DurationListMonth list : listWithMonths) {
                    if (list.getMonth() == monthValue) {
                        // Date already exists, update its duration list
                        List<DurationType> durationList = list.getDurationList();
                        boolean typeFound = false;
                        // Loop through the durationList
                        for (DurationType durationType : durationList) {
                            if (durationType.getType()  == taskType) {
                                // Task type already exists, update the duration value
                                durationType.setDuration(durationType.getDuration() + duration);
                                typeFound = true;
                                break;
                            }
                        }
                        if (!typeFound){
                            Log.d("Duration Type Error", "Some hv error");
                            break;
                        }
                        list.setDurationList(durationList);
                        monthExists = true;
                        break;
                    }
                }
                // If date does not exist, create a new DurationList and add it to listWithDate
                if (!monthExists) {
                    List<DurationType> newDurationList = new ArrayList<>();
                    newDurationList.add(new DurationType(1, 0));
                    newDurationList.add(new DurationType(2, 0));
                    newDurationList.add(new DurationType(3, 0));
                    newDurationList.add(new DurationType(4, 0));
                    newDurationList.add(new DurationType(5, 0));
                    newDurationList.add(new DurationType(6, 0));

                    for (DurationType durationType : newDurationList) {
                        if (durationType.getType()  == taskType) {
                            // Task type already exists, update the duration value
                            durationType.setDuration(durationType.getDuration() + duration);
                            break;
                        }
                    }
                    listWithMonths.add(new DurationListMonth(monthValue, newDurationList));
                }
            }while(cursor.moveToNext());
        }

        // Close the cursor and the database connection
        cursor.close();
        db.close();
        Log.d("List", "List: "+ listWithMonths);
        return listWithMonths;
    }

    public List<DurationListTask> getWeekOrMonthDataByTask(String tableName, String timeSpan, String taskName){
        List<DurationListTask> listWithDate = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate;

        if (timeSpan.equalsIgnoreCase("Week")){
            // Get the date of the week before
            startDate = currentDate.minusWeeks(1);
        } else if (timeSpan.equalsIgnoreCase("Month")) {
            // Get the date of the month before
            startDate = currentDate.minusMonths(1);
        }
        else{
            startDate = null;
            Log.d("Time Span", "Wrong Time Span entered ");
        }

        //getting dates into format for selection from db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrentDate = currentDate.format(dateFormatter);
        String formattedStartDate = startDate.format(dateFormatter);

        String selectQuerySchedule = "SELECT n.date, n.startTime, n.endTime, t.taskType FROM " + tableName +
                " n join Task t on n.taskName = t.name  WHERE n.date >= ? AND n.date <= ? AND t.name = ? ORDER BY n.date";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedStartDate, formattedCurrentDate, taskName});
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                @SuppressLint("Range") int taskType= cursor.getInt(cursor.getColumnIndex("taskType"));

                //Converting date and time to LocalDate and LocalTime
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateS, formatterDate);

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                float duration = calculateDuration(localStartTime,localEndTime);

                boolean dateExists = false;
                for (DurationListTask list : listWithDate) {
                    if (list.getDate().isEqual(localDate)) {
                        // Date already exists, update its duration
                        list.setDuration(list.getDuration()+duration);
                        dateExists = true;
                        break;
                    }
                }
                // If date does not exist, create a new DurationList and add it to listWithDate
                if (!dateExists) {
                    listWithDate.add(new DurationListTask(localDate, duration, taskType));
                }
            }while(cursor.moveToNext());
        }

        // Close the cursor and the database connection
        cursor.close();
        db.close();
        Log.d("Tasks", "getWeekOrMonthDataByTask: "+ listWithDate);
        return listWithDate;
    }

    public LocalDate getOptimalDate(String timeSpan){
        Log.d("Start", "getOptimalDate: "+ timeSpan);
        List<RecommendationCalculation> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate;

        if (timeSpan.equalsIgnoreCase("Week")){
            // Get the date of the week before
            startDate = currentDate.minusWeeks(1);
        } else if (timeSpan.equalsIgnoreCase("Month")) {
            // Get the date of the month before
            startDate = currentDate.minusMonths(1);
        } else if (timeSpan.equalsIgnoreCase("Year")) {
            // Get the date of the year before
            startDate = currentDate.minusYears(1);
        } else{
            startDate = null;
            Log.d("Time Span", "Wrong Time Span entered ");
        }

        //getting dates into format for selection from db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrentDate = currentDate.format(dateFormatter);
        String formattedStartDate = startDate.format(dateFormatter);

        String selectQuerySchedule = "SELECT date, startTime, endTime, feeling, energy FROM Progress where date >= ? AND " +
                                      "date <= ? ORDER BY date";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedStartDate, formattedCurrentDate});


        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                @SuppressLint("Range") int feeling = cursor.getInt(cursor.getColumnIndex("feeling"));
                @SuppressLint("Range") int energy = cursor.getInt(cursor.getColumnIndex("energy"));
                Log.d("Rec", "getOptimalDate: " + feeling +" "+ energy);

                //Converting date and time to LocalDate and LocalTime
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateS, formatterDate);

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                Duration duration = Duration.between(localStartTime,localEndTime);

                // Get the duration in minutes as an int
                int minutes = (int) duration.toMinutes();
                int value = feeling*energy*minutes;

                boolean dateExists = false;
                for (RecommendationCalculation calculation : list) {
                    if (calculation.getDate().isEqual(localDate)) {
                        // Date already exists, update its duration
                        calculation.setValue(calculation.getValue() + value);
                        dateExists = true;
                        break;
                    }
                }
                // If date does not exist, create a new DurationList and add it to listWithDate
                if (!dateExists) {
                    list.add(new RecommendationCalculation(localDate,value));
                }
            }while(cursor.moveToNext());
        }

        // Close the cursor and the database connection
        cursor.close();
        db.close();
        Log.d("Tasks", "get Optimal Date: "+ list);

        RecommendationCalculation obj = null;
        int max;
        if (!list.isEmpty()) {
            Log.d("ListNot", "get Optimal Date: "+ list);
            max = list.get(0).value;
            obj = list.get(0);
        } else {
            Log.d("ListEmp", "get Optimal Date: "+ list);
            max = 0;
        }
        for (RecommendationCalculation calculation : list) {
            Log.d("Loop", "get Optimal Date: "+ list);
            if (calculation.getValue() > max){
                Log.d("Loop If", "get Optimal Date: "+ list);
                max = calculation.getValue();
                obj = calculation;
            }
        }

        if (list.isEmpty()){
            Log.d("Recome", "getOptimalDate: "+ currentDate);
            return currentDate;

        }else {
            Log.d("Recome", "getOptimalDate: "+ obj.getDate());
            return obj.getDate();

        }
    }




    public List<DurationListTaskMonth> getYearDataByTask(String tableName, String taskName){
        List<DurationListTaskMonth> listWithMonths = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate;

        // Get the date of the month before
        startDate = currentDate.minusYears(1);

        //getting dates into format for selection from db
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrentDate = currentDate.format(dateFormatter);
        String formattedStartDate = startDate.format(dateFormatter);

        String selectQuerySchedule = "SELECT n.date, n.startTime, n.endTime, t.taskType FROM " + tableName +
                " n join Task t on n.taskName = t.name  WHERE n.date >= ? AND n.date <= ? AND t.name = ? ORDER BY n.date";
        Cursor cursor = db.rawQuery(selectQuerySchedule, new String[]{formattedStartDate, formattedCurrentDate, taskName});
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String dateS = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                @SuppressLint("Range") int taskType= cursor.getInt(cursor.getColumnIndex("taskType"));

                //Converting date and time to LocalDate and LocalTime
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateS, formatterDate);
                int monthValue = localDate.getMonthValue();

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                float duration = calculateDuration(localStartTime,localEndTime);

                boolean monthExists = false;
                for (DurationListTaskMonth list : listWithMonths) {
                    if (list.getMonth() == monthValue) {
                        // Date already exists, update its duration
                        list.setDuration(list.getDuration()+duration);
                        monthExists = true;
                        break;}

                }
                // If date does not exist, create a new DurationList and add it to listWithDate
                if (!monthExists) {
                    listWithMonths.add(new DurationListTaskMonth(monthValue, duration, taskType));
                }
            }while(cursor.moveToNext());
        }
        // Close the cursor and the database connection
        cursor.close();
        db.close();
        Log.d("Tasks", "getWeekOrMonthDataByTask: "+ listWithMonths);
        return listWithMonths;
    }



    public float calculateDuration(LocalTime startTime, LocalTime endTime){
        Log.d("Start", "calculateDuration: "+ startTime);
        Log.d("End", "calculateDuration: "+ endTime);
        Duration duration = Duration.between(startTime,endTime);
        float durationTime = duration.toMinutes() / 60f;
        return  durationTime;
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
      dataset.add(index, list.get(0).getDuration, list.get(1).getDuration, list.get(2).getDuration,
       list.get(3).getDuration, list.get(4).getDuration, list.get(5).getDuration)

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
