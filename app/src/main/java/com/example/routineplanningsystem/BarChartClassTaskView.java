package com.example.routineplanningsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarChartClassTaskView extends AppCompatActivity {
    private ArrayList<DurationListTask> durationListTaskArrayListSchedule;
    private ArrayList<DurationListTask> durationListTaskArrayListProgress;
    private ArrayList<DurationListTaskMonth> durationListTaskMonthArrayListSchedule;
    private ArrayList<DurationListTaskMonth> durationListTaskMonthArrayListProgress;
    private static final float barWidth = 0.1f;
    private static final String TAG = "BarChartClassTaskView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart_task_view);
        Log.d(TAG, "Finding IDs");

        BarChart chartSchedule = findViewById(R.id.chartSchedule01);
        BarChart chartProgress = findViewById(R.id.chartProgress01);
        Button buttonWeek = findViewById(R.id.buttonByWeek01);
        Button buttonMonth = findViewById(R.id.buttonByMonth01);
        Button buttonYear = findViewById(R.id.buttonByYear01);
        Button buttonFullView = findViewById(R.id.buttonFullView);
        Button menuProgressButton = findViewById(R.id.menuProgressButton);
        Button menuScheduleButton = findViewById(R.id.menuScheduleButton);
        Button menuTaskButton = findViewById(R.id.menuTaskButton);

        EditText editTextTaskView = findViewById(R.id.editTextTaskView);

        //creating arrayList for dates
        Log.d(TAG, "Start succesful");
        //FOr month and week data

    //    DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        chartsByWeekAndMonth(chartSchedule,chartProgress,"Week", "");

        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTaskView.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please Input Correct TaskName", Toast.LENGTH_SHORT).show();
                else
                    chartsByWeekAndMonth(chartSchedule, chartProgress, "Week", editTextTaskView.getText().toString());
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.categorybox));
            }
        });

        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTaskView.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please Input Correct TaskName", Toast.LENGTH_SHORT).show();
                else
                    chartsByWeekAndMonth(chartSchedule, chartProgress, "Month", editTextTaskView.getText().toString());
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.categorybox));
            }
        });

        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTaskView.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please Input Correct TaskName", Toast.LENGTH_SHORT).show();
                else
                    chartsByYear(chartSchedule,chartProgress, editTextTaskView.getText().toString());
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
            }
        });

        //for transition between by task and full view in report

        buttonFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClassTaskView.this, BarChartClass.class);
                startActivity(intent);
            }
        });

        menuProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClassTaskView.this, ProgressTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClassTaskView.this, ScheduleTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClassTaskView.this, TaskList.class);
                startActivity(intent);
            }
        });

        //need dates
        //creating database instance

        Log.d(TAG, "Final in Main succesful");
    }

    private void chartsByWeekAndMonth(BarChart chartSchedule, BarChart chartProgress, String weekOrMonth, String taskName){
        DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        //initlizing the arrays for putting data in both charts of progress and scheduels
        durationListTaskArrayListSchedule = new ArrayList<>();
        List<DurationListTask> durationListTaskArrayList = habit.getWeekOrMonthDataByTask("Schedule",weekOrMonth, taskName);
        durationListTaskArrayListSchedule.addAll(durationListTaskArrayList);
        //arrayList for schdule done
        durationListTaskArrayListProgress = new ArrayList<>();
        List<DurationListTask> durationListTaskArrayList2 = habit.getWeekOrMonthDataByTask("Progress",weekOrMonth, taskName);
        durationListTaskArrayListProgress.addAll(durationListTaskArrayList2);
        //arraylist for progress done

        Log.d(TAG, String.valueOf(durationListTaskArrayListSchedule.size()));
        Log.d(TAG, String.valueOf(durationListTaskArrayListProgress.size()));

        //creating chartSchedule and chartProgress settings
        setChartSettings(chartSchedule, chartProgress);

        //chart settign done. Need to add the width later

        Log.d(TAG, "AFte rsetting settings of chart");

        //   float barWidth = 0.4f; // Change this value as per your requirement

        //set date on xAxis along with setting the padding from sides
        //the width is set later

        setXAxisLabels(chartSchedule,durationListTaskArrayListSchedule);
        setXAxisLabels(chartProgress,durationListTaskArrayListProgress);

        Log.d(TAG, String.valueOf(durationListTaskArrayListSchedule.size()) + "After Date setting on X Axis");
        Log.d(TAG, String.valueOf(durationListTaskArrayListProgress.size()) + "After Date setting on X Axis");


        //setting color and values
        //data needs to be compatabile with the barChart
        //can be float or int
        //customize valueFormatting with valueFormatter to chagne into toehr types
        Log.d(TAG, "Before barEntries");
        Log.d(TAG, durationListTaskArrayListSchedule.size()+" ");
        Log.d(TAG, durationListTaskArrayListProgress.size()+" ");

        //enter barEntries
        //put entries in format in according to workType
        //    setBarEntries(durationListTaskArrayListSchedule);
        List<BarEntry> barEntriesScheduleChart =  setBarEntries(durationListTaskArrayListSchedule);
        List<BarEntry> barEntriesProgressChart =  setBarEntries(durationListTaskArrayListProgress);


//for task colors

        Log.d(TAG, "Reached after adding barentries");
        Log.d(TAG, "Size: " + barEntriesScheduleChart.size());
        Log.d(TAG, "Size: " + barEntriesProgressChart.size());

        Log.d(TAG, "Almost last");

        setChartWithColorsAndData(barEntriesScheduleChart, chartSchedule);
        setChartWithColorsAndData(barEntriesProgressChart,chartProgress);
        Log.d(TAG, "Last");
    }

    private void chartsByYear(BarChart chartSchedule, BarChart chartProgress, String taskName){
        DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        //initlizing the arrays for putting data in both charts of progress and scheduels
        durationListTaskMonthArrayListSchedule = new ArrayList<>();
        List<DurationListTaskMonth> durationListTaskArrayList = habit.getYearDataByTask("Schedule", taskName);
        durationListTaskMonthArrayListSchedule.addAll(durationListTaskArrayList);
        //arrayList for schdule done
        durationListTaskMonthArrayListProgress = new ArrayList<>();
        List<DurationListTaskMonth> durationListTaskArrayList2 = habit.getYearDataByTask("Progress", taskName);
        durationListTaskMonthArrayListProgress.addAll(durationListTaskArrayList2);
        //arraylist for progress done

        Log.d(TAG, String.valueOf(durationListTaskMonthArrayListSchedule.size()));
        Log.d(TAG, String.valueOf(durationListTaskMonthArrayListProgress.size()));

        //creating chartSchedule and chartProgress settings
        setChartSettings(chartSchedule, chartProgress);

        //chart settign done. Need to add the width later

        Log.d(TAG, "AFte rsetting settings of chart");

        //   float barWidth = 0.4f; // Change this value as per your requirement
        //set date on xAxis along with setting the padding from sides
        //the width is set later

        setXAxisLabelsForYear(chartSchedule,durationListTaskMonthArrayListSchedule);
        setXAxisLabelsForYear(chartProgress,durationListTaskMonthArrayListProgress);

        Log.d(TAG, String.valueOf(durationListTaskMonthArrayListSchedule.size()) + "After Date setting on X Axis");
        Log.d(TAG, String.valueOf(durationListTaskMonthArrayListProgress.size()) + "After Date setting on X Axis");


        //setting color and values
        //data needs to be compatabile with the barChart
        //can be float or int
        //customize valueFormatting with valueFormatter to chagne into toehr types
        Log.d(TAG, "Before barEntries");
        Log.d(TAG, durationListTaskMonthArrayListSchedule.size()+" ");
        Log.d(TAG, durationListTaskMonthArrayListProgress.size()+" ");

        //enter barEntries
        //put entries in format in according to workType
        //wrote by error/?  setBarEntries(durationListTaskArrayListSchedule);
        List<BarEntry> barEntriesScheduleChart =  setBarEntriesForYear(durationListTaskMonthArrayListSchedule);
        List<BarEntry> barEntriesProgressChart =  setBarEntriesForYear(durationListTaskMonthArrayListProgress);


//for task colors

        Log.d(TAG, "Reached after adding barentries");
        Log.d(TAG, "Size: " + barEntriesScheduleChart.size());
        Log.d(TAG, "Size: " + barEntriesProgressChart.size());

        Log.d(TAG, "Almost last");

        setChartWithColorsAndData(barEntriesScheduleChart, chartSchedule);
        setChartWithColorsAndData(barEntriesProgressChart,chartProgress);
        Log.d(TAG, "Last");
    }
    private void setChartSettings(BarChart chartSchedule, BarChart chartProgress) {
        chartSchedule.clear(); //for clearing existing data from chartSchedule
        chartSchedule.setDrawBarShadow(false);
        chartSchedule.setDrawValueAboveBar(true);
        chartSchedule.getDescription().setEnabled(false);
        chartSchedule.setBackgroundColor(getResources().getColor(R.color.prominent_boxes));

        chartProgress.clear(); //for clearing existing data from chartProgress
        chartProgress.setDrawBarShadow(false);
        chartProgress.setDrawValueAboveBar(true);
        chartProgress.getDescription().setEnabled(false);
        chartProgress.setBackgroundColor(getResources().getColor(R.color.prominent_boxes));
    }

    public void setXAxisLabels(BarChart barChart, ArrayList<DurationListTask> durationListTaskArrayList){
        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();// Get the y-axis object
        xAxis.setTextColor(Color.WHITE);

        yAxisLeft.setTextColor(Color.WHITE); // Set the text color for y-axis labels
        yAxisRight.setTextColor(Color.WHITE);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd");

            @Override
            public String getFormattedValue(float value) {
                // Convert the float value to an index
                int index = (int) value;

                // Check if the index is within the bounds of the dates array
                if (index >= 0 && index < durationListTaskArrayList.size()) {
                    // Get the corresponding date from the dates array
                    LocalDate date = durationListTaskArrayList.get(index).getDate();
                    Log.d(TAG, "Called Date" +durationListTaskArrayList.get(index).getDate());
                    // Format the date using SimpleDateFormat
                    return sdf.format(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }

                // Return an empty string if the index is out of bounds
                return "";
            }
        });
        float paddingSpace = 0.2f;
// Set the x-axis limits with added padding
        xAxis.setAxisMinimum(-paddingSpace - barWidth / 2f);
        xAxis.setAxisMaximum(durationListTaskArrayList.size() - 1 + paddingSpace);
    }

    public void setXAxisLabelsForYear(BarChart barChart, ArrayList<DurationListTaskMonth> durationListTaskArrayList){
        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();// Get the y-axis object
        xAxis.setTextColor(Color.WHITE);

        yAxisLeft.setTextColor(Color.WHITE); // Set the text color for y-axis labels
        yAxisRight.setTextColor(Color.WHITE);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("MMM");

            @Override
            public String getFormattedValue(float value) {
                // Convert the float value to an index
                int index = (int) value;

                // Check if the index is within the bounds of the dates array
                if (index >= 0 && index < durationListTaskArrayList.size()) {
                    // Get the corresponding date from the dates array\
                    //returing int instead of month
                    int monthValue = durationListTaskArrayList.get(index).getMonth();
                    int year = 2023; // Replace with the actual year value
                    int dayOfMonth = 1; // Replace with the actual day of the month value

                    LocalDate date = LocalDate.of(year, monthValue, dayOfMonth);

                    Log.d(TAG, "Called Date" +durationListTaskArrayList.get(index).getMonth());
                    Log.d(TAG, "Called Date" +LocalDate.ofEpochDay(durationListTaskArrayList.get(index).getMonth()));

                    // Format the date using SimpleDateFormat
                    return sdf.format(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }

                // Return an empty string if the index is out of bounds
                return "";
            }
        });
        float paddingSpace = 0.2f;
// Set the x-axis limits with added padding
        xAxis.setAxisMinimum(-paddingSpace - barWidth / 2f);
        xAxis.setAxisMaximum(durationListTaskArrayList.size() - 1 + paddingSpace);
    }

    private List<BarEntry> setBarEntries(ArrayList <DurationListTask> durationListTaskArrayList){
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < durationListTaskArrayList.size(); i++) {
            DurationListTask localDurationListTask = durationListTaskArrayList.get(i);
            if(localDurationListTask.getTaskType() == 1) {
                barEntries.add(new BarEntry(i, new float[]{localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "1", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 2) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "2", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 3) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "3", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 4) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f}));
                Log.d(TAG + "4", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 5) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f}));
                Log.d(TAG + "5", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 6) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, localDurationListTask.getDuration()}));
                Log.d(TAG + "6", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else //A tag to know the it is now in else
                Log.d(TAG + "7", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
        }
        return barEntries;
    }

    private List<BarEntry> setBarEntriesForYear(ArrayList <DurationListTaskMonth> durationListTaskArrayList){
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < durationListTaskArrayList.size(); i++) {
            DurationListTaskMonth localDurationListTask = durationListTaskArrayList.get(i);

            if(localDurationListTask.getTaskType() == 1) {
                barEntries.add(new BarEntry(i, new float[]{localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "1", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 2) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "2", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 3) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f, 0.0f}));
                Log.d(TAG + "3", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 4) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f, 0.0f}));
                Log.d(TAG + "4", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 5) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, 0.0f, localDurationListTask.getDuration(), 0.0f}));
                Log.d(TAG + "5", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else if(localDurationListTask.getTaskType() == 6) {
                barEntries.add(new BarEntry(i, new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, localDurationListTask.getDuration()}));
                Log.d(TAG + "6", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
            }
            else //A tag to know the it is now in else
                Log.d(TAG + "7", localDurationListTask.getDuration() + "" +  localDurationListTask.getTaskType());
        }
        return barEntries;
    }

    private void setChartWithColorsAndData(List<BarEntry> barEntries, BarChart barChart){
        BarDataSet dataSet = new BarDataSet(barEntries, null);
// Set color for each entry based on its category
        dataSet.setColors(getResources().getColor(R.color.work), getResources().getColor(R.color.sleep),getResources().getColor(R.color.spiritual),getResources().getColor(R.color.relax),getResources().getColor(R.color.development),getResources().getColor(R.color.social)); //doesn't recognize border it seems
        dataSet.setStackLabels(new String[]{"Work", "Sleep", "Spiritual", "Relax", "Development", "Social"});
        dataSet.setValueTextColor(Color.WHITE); // Set the text color for data values
        //color for bottom legend
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.WHITE);

        //setting chartChart to bardata
        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);
        // data.groupBars(0,1,2);
        barChart.setData(data);
        barChart.invalidate(); //additonal function for redrawal of graph with the new datait seems
        barChart.animateY(1000);
    }

}