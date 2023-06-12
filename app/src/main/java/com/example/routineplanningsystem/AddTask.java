package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AddTask extends AppCompatActivity {

    String taskCategory;
    int taskType = 0;
    View nameBox;
    TextView categoryText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        DBHelper dbHelper = new DBHelper(this, "habit",null, 1);

//        dbHelper.insertTask(new Task("Work", null, 2));
//        dbHelper.insertTask(new Task("Assignment", null, 2));
//        dbHelper.insertTask(new Task("Quiz", null, 2));
//        List<Task> list = dbHelper.getAllTasks();
//        Log.d("List Size", "onCreate: "+ list.size());
        EditText nameEditBox, descriptionEditBox;
        Button btn1, btn2, btn3, btn4, btn5, btn6, addButton, menuTaskButton, menuScheduleButton, menuProgressButton, menuReportButton;

        menuTaskButton = findViewById(R.id.menuTaskButton);
        menuScheduleButton = findViewById(R.id.menuScheduleButton);
        menuProgressButton = findViewById(R.id.menuProgressButton);
        menuReportButton = findViewById(R.id.menuReportButton);

        nameEditBox = findViewById(R.id.taskNameEditText);
        descriptionEditBox = findViewById(R.id.descriptionEditText);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);

        addButton = findViewById(R.id.addButton);

        categoryText = findViewById(R.id.categoryTextView);

        nameBox = findViewById(R.id.nameBox);

//        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(this, );
//
//
//            }
//        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  setTaskCategory(R.color.work,"Work",1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskCategory(R.color.sleep,"Sleep",2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskCategory(R.color.spiritual,"Spirtual",3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskCategory(R.color.relax,"Relax",4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskCategory(R.color.development,"Development",5);}
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskCategory(R.color.social,"Social",6);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskNameString = nameEditBox.getText().toString().trim();
                String taskDescriptionString = nameEditBox.getText().toString().trim();
                boolean alreadyExists = dbHelper.checkTask(taskNameString);
                if (!taskNameString.isEmpty()){
                    if (taskType!=0 ){
                        if (!alreadyExists){
                            boolean success = dbHelper.insertTask(new Task(taskNameString,taskDescriptionString,taskType));
                            if (success){
                                Toast.makeText(AddTask.this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                                nameEditBox.setText("");
                                descriptionEditBox.setText("");
                            }
                            else{
                                Toast.makeText(AddTask.this, "Task Adding Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AddTask.this, "Task Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddTask.this, "Please select Category", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AddTask.this, "Please enter TaskName", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setTaskCategory(int backgroundColor, String category, int type) {
        nameBox.setBackgroundColor(getResources().getColor(backgroundColor));
        taskCategory = category;
        taskType = type;
        categoryText.setText(taskCategory);
    }

}