package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class TaskList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterTask recyclerViewAdapterTask;
    private ArrayList<Task> taskArrayList;
    private ArrayAdapter<String> arrayAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper habit = new DBHelper(this, "db",null, 1);
    // Test for taskListGetting   Task task1 = new Task("1", null ,2) //Task task2 = new Task("2", null ,2) habit.insertTask(task1);  habit.insertTask(task2);
        Task task1 = new Task("11", null ,1);
        Task task2 = new Task("12", null ,1);

        habit.insertTask(task1);  habit.insertTask(task2);
//for insertion of task list into taskArrayList
        taskArrayList = new ArrayList<>();
        List<Task> taskList = habit.getAllTasks();
        for(Task task : taskList){
            taskArrayList.add(task);
        }
//for the recycleViewAdapter
        recyclerViewAdapterTask = new RecyclerViewAdapterTask(TaskList.this,taskArrayList);
        recyclerView.setAdapter(recyclerViewAdapterTask);


    }

}