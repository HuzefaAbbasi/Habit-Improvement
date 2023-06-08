package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    String taskCategory;
    int taskType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        DBHelper dbHelper = new DBHelper(this, "habit",null, 1);

        EditText nameEditBox, descriptionEditBox;
        Button btn1, btn2, btn3, btn4, btn5, btn6, addButton;
        TextView categoryText;
        View nameBox;

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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.work));
                taskCategory = "Work";
                taskType = 1;
                categoryText.setText(taskCategory);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.sleep));
                taskCategory = "Sleep";
                taskType = 2;
                categoryText.setText(taskCategory);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.spiritual));
                taskCategory = "Spirtual";
                taskType = 3;
                categoryText.setText(taskCategory);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.relax));
                taskCategory = "Relax";
                taskType = 4;
                categoryText.setText(taskCategory);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.development));
                taskCategory = "Development";
                taskType = 5;
                categoryText.setText(taskCategory);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBox.setBackgroundColor(getResources().getColor(R.color.social));
                taskCategory = "Social";
                taskType = 6;
                categoryText.setText(taskCategory);
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
}