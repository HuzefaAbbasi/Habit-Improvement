package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public class AddProgress extends AppCompatActivity {

    int duration = 0;
    List<String> taskNameList = new ArrayList<>();
    List<Task> taskList = new ArrayList<>();
    Stack<Integer> durationStack = new Stack<>();
    TextView dateTextView, startTimeTextView, durationTextView;
    AutoCompleteTextView taskAutoComplete;
    Button calendarButton, clockButton, taskSelectButton, plus5Button, plus15Button, plus30Button, plus60Button, removeDuration;
    Button addProgress, feelingButton, energyButton;
    boolean dateCheck = false, taskCheck = false, startTimeCheck = false;
    LocalTime startTime;
    LocalTime endTime;
    LocalDate date;
    Task selectedTask;
    int feelingValue = -1;
    int energyValue = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);


        calendarButton = findViewById(R.id.calendarButton);
        clockButton = findViewById(R.id.clockButton);
        taskSelectButton = findViewById(R.id.taskSelectButton);

        plus5Button = findViewById(R.id.plus5Button);
        plus15Button = findViewById(R.id.plus15Button);
        plus30Button = findViewById(R.id.plus30Button);
        plus60Button = findViewById(R.id.plus60Button);
        removeDuration = findViewById(R.id.removeDurationButton);

        addProgress = findViewById(R.id.addProgressButton);

        feelingButton = findViewById(R.id.feelingButton);
        energyButton = findViewById(R.id.energyButton);

        durationTextView = findViewById(R.id.durationTextView);
        dateTextView = findViewById(R.id.dateTextView);
        startTimeTextView = findViewById(R.id.startTextView);

        taskAutoComplete = findViewById(R.id.taskAutoTextView);
        Button menuTaskButton = findViewById(R.id.menuTaskButton);
        Button menuScheduleButton = findViewById(R.id.menuScheduleButton);
        Button menuReportButton = findViewById(R.id.menuReportButton);

        Spinner feelingSpinner = findViewById(R.id.spinner);
        Spinner energySpinner = findViewById(R.id.spinner2);

        //For Feeling Spinner
        // Create a data source
        List<String> feelingList = new ArrayList<>();
        feelingList.add("Very Good");
        feelingList.add("Good");
        feelingList.add("Fair");
        feelingList.add("Bad");
        feelingList.add("Very Bad");

        // Create an ArrayAdapter using the data source
        ArrayAdapter<String> adapterFeeling = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, feelingList);

        // Set the ArrayAdapter to the Spinner
        feelingSpinner.setAdapter(adapterFeeling);

        //For Energy Spinner
        // Create a data source
        List<String> energyList = new ArrayList<>();
        energyList.add("High");
        energyList.add("Moderate");
        energyList.add("Low");

        // Create an ArrayAdapter using the data source
        ArrayAdapter<String> adapterEnergy = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, energyList);

        // Set the ArrayAdapter to the Spinner
        energySpinner.setAdapter(adapterEnergy);


        DBHelper dbHelper = new DBHelper(this, "habit",null, 1);

//        List<Schedule> list = dbHelper.getAllScheduleForDate(LocalDate.of(2023,5,4));
//        dbHelper.insertTask(new Task("Project",null, 5));
//        dbHelper.insertTask(new Task("Android",null, 1));
//        dbHelper.insertTask(new Task("Sleep",null, 4));
        taskList = dbHelper.getAllTasks();
        for (Task task : taskList){
            taskNameList.add(task.getTaskName());
        }

        //Creating auto-complete EditTextView for taking task input
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, taskNameList);
        taskAutoComplete.setAdapter(adapter);


        // plus times buttons onClicks
        plus5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration += 5;
                durationStack.push(5);
                durationTextView.setText(duration + " min");
            }
        });
        plus15Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration += 15;
                durationStack.push(15);
                durationTextView.setText(duration + " min");
            }
        });
        plus30Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration += 30;
                durationStack.push(30);
                durationTextView.setText(duration + " min");
            }
        });
        plus60Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration += 60;
                durationStack.push(60);
                durationTextView.setText(duration + " min");
            }
        });

        //Remove duration button removes previously pushed duration.
        removeDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removedValue;
                if(!durationStack.isEmpty()){
                    removedValue = durationStack.pop();
                    duration-= removedValue;
                } else{
                    duration = 0;
                }
                durationTextView.setText(duration + " min");
            }
        });

        //When button is clicked all list is displayed
        taskSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dropdown options
                taskAutoComplete.showDropDown();
            }
        });

        //Date Dialog
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProgress.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Handle the selected date
                                String selectedDate = dayOfMonth + "/ " + (month + 1) + "/ " + year;
                                dateTextView = findViewById(R.id.dateTextView);
                                dateTextView.setText(selectedDate);
                                date = LocalDate.of(year, month+1, dayOfMonth);
                                dateCheck = true;
                            }
                        }, year, month, dayOfMonth);
                //Disabling past dates
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(calendar.getTimeInMillis());

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });
        //Clock Dialog
        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddProgress.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm;
                                int hour;

                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                    hour = (hourOfDay == 12) ? 12 : hourOfDay - 12;
                                } else {
                                    amPm = "AM";
                                    hour = (hourOfDay == 0) ? 12 : hourOfDay;
                                }

                                // Handle the selected time
                                String selectedTime = String.format(Locale.US, "%02d:%02d %s", hour, minute, amPm);
                                startTimeTextView = findViewById(R.id.startTextView);
                                startTimeTextView.setText(selectedTime);
                                startTime = LocalTime.of(hourOfDay, minute);
                                Log.d("StartTime","Time: "+ startTime);
                                startTimeCheck = true;
                            }
                        }, hour, minute, false); // Use false for 12-hour format or true for 24-hour format

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        taskAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String selectedValue = taskAutoComplete.getText().toString();
                Map<Integer , Integer> colorVal = new HashMap<>();
                colorVal.put(1, R.color.work);
                colorVal.put(2, R.color.sleep);
                colorVal.put(3, R.color.spiritual);
                colorVal.put(4, R.color.relax);
                colorVal.put(5, R.color.development);
                colorVal.put(6, R.color.social);
                // Check the selected value and update the text color accordingly
                // task that user selects from auto complete
                String enteredTask = taskAutoComplete.getText().toString().trim().toLowerCase();
                for (Task task : taskList){
                    String str = task.getTaskName().trim().toLowerCase();
                    if (enteredTask.equals(str)) {
                        taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), colorVal.get(task.getTaskType())));
//                        if (task.getTaskType() == 1) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.work));
//                            break;
//                        } else if (task.getTaskType() == 2) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.sleep));
//                            break;
//                        } else if (task.getTaskType() == 3) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.spiritual));
//                            break;
//                        } else if (task.getTaskType() == 4) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.relax));
//                            break;
//                        } else if (task.getTaskType() == 5) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.development));
//                            break;
//                        } else if (task.getTaskType() == 6) {
//                            taskAutoComplete.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.social));
//                            break;
//                        }
                    }
                }
            }
        });

        feelingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feelingSpinner.performClick();
            }
        });

        menuTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProgress.this, TaskList.class);
                startActivity(intent);
            }
        });

        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProgress.this, ScheduleTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProgress.this, BarChartClass.class);
                startActivity(intent);
            }
        });


        energyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                energySpinner.performClick();
            }
        });
        //Getting value of feeling, and energy in int
        feelingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the position of the selected item, starts from zero
                feelingValue = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        energySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the position of the selected item, starts from zero
                energyValue = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });




        addProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifying all data is given properly
                if (dateCheck){
                    if (!TextUtils.isEmpty(taskAutoComplete.getText())){
                        // task that user selects from auto complete
                        String enteredTask = taskAutoComplete.getText().toString().trim().toLowerCase();
                        //Checking that selected task is from the list
                        for (String listItem : taskNameList){
                            if (enteredTask.equals(listItem.trim().toLowerCase())){
                                taskCheck = true;
                                break;
                            }
                        }
                        if (taskCheck){
                            if (feelingValue >= 0){
                                if (energyValue >= 0){
                                    if (startTimeCheck){
                                        if (duration > 0 && duration <= 600) {
                                            endTime = startTime.plusMinutes(duration);
                                            boolean checkDuration = dbHelper.checkDate("Progress",date,startTime,endTime);
                                            Log.d("checkDuration", "Check: "+checkDuration);
                                            if(checkDuration){
                                                if (!endTime.isBefore(startTime)){
                                                    endTime = startTime.plusMinutes(duration);
                                                    //Selecting task which has entered task name
                                                    for (Task task : taskList){
                                                        String str = task.getTaskName().trim().toLowerCase();
                                                        if(enteredTask.equals(str)){
                                                            selectedTask = task;
                                                            break;
                                                        }
                                                    }

                                                    Progress progress = new Progress(date, startTime, endTime, selectedTask,
                                                            energyValue+1, feelingValue+1);
                                                    boolean success = dbHelper.insertProgress(progress);

                                                    if (success){
                                                        Toast.makeText(AddProgress.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                                        dateTextView.setText("Select Date");
                                                        startTimeTextView.setText("Select Time");
                                                        durationTextView.setText("0 min");
                                                        taskAutoComplete.setText(null);
                                                        feelingSpinner.setSelection(0);
                                                        energySpinner.setSelection(0);
                                                        duration = 0;
                                                        durationStack.clear();
                                                    }
                                                    else {
                                                        Toast.makeText(AddProgress.this, "Progress Adding Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(AddProgress.this, "Please Enter Progress for Current Date Only", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            else{
                                                Toast.makeText(AddProgress.this, "Progress already Exists", Toast.LENGTH_SHORT).show();
                                            }

                                        } else{
                                            Toast.makeText(AddProgress.this, "Please Add Correct Duration", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(AddProgress.this, "Start Time not Selected", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(AddProgress.this, "Energy not Selected", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(AddProgress.this, "Feeling not Selected", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddProgress.this, "Task not Exist", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(AddProgress.this, "Task not Selected", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(AddProgress.this, "Date not Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press here
        // Perform your desired action or navigation

        // If you want to keep the default behavior (e.g., navigate back),
        // you can call the super method
        Intent intent = new Intent(AddProgress.this, ProgressTabLayOutView.class);
        startActivity(intent);
    }
}