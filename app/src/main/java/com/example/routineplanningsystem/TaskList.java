package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterTask recyclerViewAdapterTask;
    private ArrayList<Task> taskArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private static final String TAG = "TaskListClass";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button goToAddTaskButton = findViewById(R.id.goToAddTaskButton);
        Button menuProgressButton = findViewById(R.id.menuProgressButton);
        Button menuScheduleButton = findViewById(R.id.menuScheduleButton);
        Button menuReportButton = findViewById(R.id.menuReportButton);


        DBHelper habit = new DBHelper(this, "habit",null, 1);
        // Test for taskListGetting   Task task1 = new Task("1", null ,2) //Task task2 = new Task("2", null ,2) habit.insertTask(task1);  habit.insertTask(task2);

//for insertion of task list into taskArrayList
        taskArrayList = new ArrayList<>();
        List<Task> taskList = habit.getAllTasks();
        Log.d(TAG, taskList.size()+"");

        taskArrayList.addAll(taskList);

//for the recycleViewAdapter
        recyclerViewAdapterTask = new RecyclerViewAdapterTask(TaskList.this, taskArrayList,habit);
        recyclerView.setAdapter(recyclerViewAdapterTask);

        goToAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskList.this, AddTask.class);
                startActivity(intent);
            }
        });

        menuProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskList.this, ProgressTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskList.this, ScheduleTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskList.this, BarChartClass.class);
                startActivity(intent);
            }
        });

    }

}