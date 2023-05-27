package com.example.routineplanningsystem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
        String create = "Create Table Task " +
                "(name varchar(50) PRIMARY KEY, " +
                "description varchar(2000)," +
                "taskType int NOT NULl)";


        String create1 = "Create Table SubTask (" +
                "SubTaskName varchar(100) PRIMARY KEY," +
                "Completed int," +
                "FOREIGN KEY(name) REFERENCES Task(name)" +
                ");"; //foriegn keys at end?


        String create2 = "Create Table Schedule (" +
                "Time TIME," +
                "Date DATE," +
                // "Random varchar(50)" +
                "PRIMARY KEY(Time, Date)," +
                "FOREIGN KEY(name) REFERENCES Task(name)" +
                ");";

        String create3 = "Create Table Progress (" +
                "Time TIME," +
                "Date DATE," +
                "name varchar(50)," +
                "EnumEnergy int CONSTRAINT energy_check CHECK (EnumEnergy BETWEEN 0 AND 10)," +
                "EnumFeeling int CONSTRAINT feeling_check CHECK (EnumFeeling BETWEEN 0 AND 10)," +
                "PRIMARY KEY(Time, Date)," +
                "FOREIGN KEY(name) REFERENCES Task(name)" +

                // "EnumFeeling int" +
                ");";



        db.execSQL(create);
        //  db.execSQL(create1);
        //  db.execSQL(create2);
        //db.execSQL(create3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(drop, new String[]{"myUser"});
        onCreate(db);
    }

    public void insertTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskType" , task.getTaskType());
        values.put("name", task.getTaskName());
        values.put("description", task.getTaskDescription());
        long k = db.insert("Task", null,values);
        Log.d("mytag", Long.toString(k));
        db.close();
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();


        String TaskName = String.valueOf(task.getTaskName());
        String[] whereargs = {TaskName};
        int k = db.delete("Task",
                "name = ?",
                whereargs);
        Log.d("mtag1", Integer.toString(k));
    }

    public void updateTask(Task oldtask, Task newTask){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",newTask.getTaskName());
        contentValues.put("description", newTask.getTaskDescription());
        contentValues.put("taskType" , newTask.getTaskType());

        String[] whereargs = {oldtask.getTaskName()};
        int rowsUpdates = db.update("Task", contentValues,"name = ?", whereargs);
        Log.d("myTag2", Integer.toString(rowsUpdates));
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT name, taskEnum FROM Task";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int taskEnumOrdinal = cursor.getInt(cursor.getColumnIndex("taskEnum"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                //  String description = cursor.getString(cursor.getColumnIndex("description"));
//                Task task = new Task(TaskEnum.values()[taskEnumOrdinal], name);
//                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    //For Schedule

    /*
       "Time TIME," +
                "Date DATE," +
               // "Random varchar(50)" +
                "PRIMARY KEY(Time, Date)," +
                "FOREIGN KEY(name) REFERENCES Task(name)" +
                ");";

        String create3 = "Create Table Progress (" +
                "Time TIME," +
                "Date DATE," +
                "name varchar(50)," +
                "Energy int CONSTRAINT energy_check CHECK (EnumEnergy BETWEEN 0 AND 10)," +
                "EnumFeeling int CONSTRAINT feeling_check CHECK (EnumFeeling BETWEEN 0 AND 10)," +
                "PRIMARY KEY(Time, Date)," +
                "FOREIGN KEY(name) REFERENCES Task(name)" +



    public void insertRoutine(Task task, LocalDate localDate, LocalTime localTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("Date", String.valueOf(localDate));
        values.put("Time", String.valueOf(localTime));
        long k = db.insert("Schedule", null,values);
        Log.d("mytag11", Long.toString(k));
        db.close();
    }

    public void deleteRoutine(Task task, LocalDate localDate, LocalTime localTime) {
        SQLiteDatabase db = this.getWritableDatabase();


        String TaskName = String.valueOf(task.getName());
        String Date = String.valueOf(localDate);
        String Time = String.valueOf(localTime);
        String[] whereargs = {TaskName, Date, Time};
       // String[] whereargs2 = {Date};
       // String[] whereargs3 = {Time};

        int k = db.delete("Schedule",
                "name = ? AND Date = ? AND Time = ?",
                whereargs);
        Log.d("mtag12", Integer.toString(k));
    }

    public void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",newTask.getName());
        contentValues.put("Date",String.valueOf(newLocaLdate));
        contentValues.put("Time", String.valueOf(newLocalTime));

        String[] whereargs = {oldTask.getName(), String.valueOf(oldLocalDate), String.valueOf(oldLocalTime)};
        int rowsUpdates = db.update("Schedule", contentValues,
                "name = ? AND Date = ? AND Time = ?",
                whereargs);
        Log.d("myTag13", Integer.toString(rowsUpdates));
    }



//-------------------------

    public void insertRoutine(Task task, LocalDate localDate, LocalTime localTime){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("name", task.getName());
    values.put("Date", String.valueOf(localDate));
    values.put("Time", String.valueOf(localTime));
    long k = db.insert("Schedule", null,values);
    Log.d("mytag21", Long.toString(k));
    db.close();
}

    public void deleteRoutine(Task task, LocalDate localDate, LocalTime localTime) {
        SQLiteDatabase db = this.getWritableDatabase();


        String TaskName = String.valueOf(task.getName());
        String Date = String.valueOf(localDate);
        String Time = String.valueOf(localTime);
        String[] whereargs = {TaskName, Date, Time};
        // String[] whereargs2 = {Date};
        // String[] whereargs3 = {Time};

        int k = db.delete("Schedule",
                "name = ? AND Date = ? AND Time = ?",
                whereargs);
        Log.d("mtag22", Integer.toString(k));
    }

    public void updateRoutine(Task oldTask, Task newTask, LocalDate oldLocalDate, LocalDate newLocaLdate, LocalTime oldLocalTime, LocalTime newLocalTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",newTask.getName());
        contentValues.put("Date",String.valueOf(newLocaLdate));
        contentValues.put("Time", String.valueOf(newLocalTime));

        String[] whereargs = {oldTask.getName(), String.valueOf(oldLocalDate), String.valueOf(oldLocalTime)};
        int rowsUpdates = db.update("Schedule", contentValues,
                "name = ? AND Date = ? AND Time = ?",
                whereargs);
        Log.d("myTag23", Integer.toString(rowsUpdates));
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
