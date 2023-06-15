package com.example.routineplanningsystem;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarChartClass extends AppCompatActivity {
    private ArrayList<DurationList> durationListArrayListSchedule;
    private ArrayList<DurationList> durationListArrayListProgress;
    private ArrayList<DurationListMonth> durationListMonthArrayListSchedule;
    private ArrayList<DurationListMonth> durationListMonthArrayListProgress;
    private static final float barWidth = 0.1f;
    private static final String TAG = "BarChartClass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart_full_view);
        Log.d(TAG, "Finding IDs");

        BarChart chartSchedule = findViewById(R.id.chartSchedule01);
        BarChart chartProgress = findViewById(R.id.chartProgress01);
        Button buttonWeek = findViewById(R.id.buttonByWeek01);
        Button buttonMonth = findViewById(R.id.buttonByMonth01);
        Button buttonYear = findViewById(R.id.buttonByYear01);
        Button buttonTaskView = findViewById(R.id.buttonFullView);
        Button menuProgressButton = findViewById(R.id.menuProgressButton);
        Button menuScheduleButton = findViewById(R.id.menuScheduleButton);
        Button menuTaskButton = findViewById(R.id.menuTaskButton);



//creating arrayList for dates
        Log.d(TAG, "Start succesful");
        //FOr month and week data
      //  DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        chartsByWeekAndMonth(chartSchedule,chartProgress,"Week");

        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByWeekAndMonth(chartSchedule,chartProgress,"Week");
                // Set the new drawable as the background of the button (deprecated)
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.categorybox));
            }
        });

        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByWeekAndMonth(chartSchedule,chartProgress,"Month");
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.categorybox));
            }
        });

        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByYear(chartSchedule,chartProgress);
                buttonWeek.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonMonth.setBackground(getResources().getDrawable(R.drawable.categorybox));
                buttonYear.setBackground(getResources().getDrawable(R.drawable.addtaskbutton));
            }
        });
//for transition between by task and full view in report
        buttonTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClass.this, BarChartClassTaskView.class);
                startActivity(intent);
            }
        });

        //need dates
        //creating database instance

        menuProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClass.this, ProgressTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClass.this, ScheduleTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarChartClass.this, TaskList.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "Final in Main succesful");
    }

    private void chartsByWeekAndMonth(BarChart chartSchedule, BarChart chartProgress, String weekOrMonth){
        DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        //initlizing the arrays for putting data in both charts of progress and scheduels
        durationListArrayListSchedule = new ArrayList<>();
        List<DurationList> durationListArrayList = habit.getWeekOrMonthData("Schedule",weekOrMonth);
        durationListArrayListSchedule.addAll(durationListArrayList);
        //arrayList for schdule done
        durationListArrayListProgress = new ArrayList<>();
        List<DurationList> durationListArrayList2 = habit.getWeekOrMonthData("Progress",weekOrMonth);
        durationListArrayListProgress.addAll(durationListArrayList2);
        //arraylist for progress done

        Log.d(TAG, String.valueOf(durationListArrayListSchedule.size()));
        Log.d(TAG, String.valueOf(durationListArrayListProgress.size()));

        //creating chartSchedule and chartProgress settings
        setChartSettings(chartSchedule, chartProgress);

        //chart settign done. Need to add the width later

        Log.d(TAG, "AFte rsetting settings of chart");

        //   float barWidth = 0.4f; // Change this value as per your requirement

        //set date on xAxis along with setting the padding from sides
        //the width is set later

        setXAxisLabels(chartSchedule,durationListArrayListSchedule);
        setXAxisLabels(chartProgress,durationListArrayListProgress);

        Log.d(TAG, String.valueOf(durationListArrayListSchedule.size()) + "After Date setting on X Axis");
        Log.d(TAG, String.valueOf(durationListArrayListProgress.size()) + "After Date setting on X Axis");


        //setting color and values
        //data needs to be compatabile with the barChart
        //can be float or int
        //customize valueFormatting with valueFormatter to chagne into toehr types
        Log.d(TAG, "Before barEntries");
        Log.d(TAG, durationListArrayListSchedule.size()+" ");
        Log.d(TAG, durationListArrayListProgress.size()+" ");

        //enter barEntries
        //put entries in format in according to workType
        //    setBarEntries(durationListArrayListSchedule);
        List<BarEntry> barEntriesScheduleChart =  setBarEntries(durationListArrayListSchedule);
        List<BarEntry> barEntriesProgressChart =  setBarEntries(durationListArrayListProgress);


//for task colors

        Log.d(TAG, "Reached after adding barentries");
        Log.d(TAG, "Size: " + barEntriesScheduleChart.size());
        Log.d(TAG, "Size: " + barEntriesProgressChart.size());

        Log.d(TAG, "Almost last");

        setChartWithColorsAndData(barEntriesScheduleChart, chartSchedule);
        setChartWithColorsAndData(barEntriesProgressChart,chartProgress);
        Log.d(TAG, "Last");
    }

    private void chartsByYear(BarChart chartSchedule, BarChart chartProgress){
        DBHelper habit = new DBHelper(this, "habit", null, 1); //check

        //initlizing the arrays for putting data in both charts of progress and scheduels
        durationListMonthArrayListSchedule = new ArrayList<>();
        List<DurationListMonth> durationListArrayList = habit.getYearData("Schedule");
        durationListMonthArrayListSchedule.addAll(durationListArrayList);
        //arrayList for schdule done
        durationListMonthArrayListProgress = new ArrayList<>();
        List<DurationListMonth> durationListArrayList2 = habit.getYearData("Progress");
        durationListMonthArrayListProgress.addAll(durationListArrayList2);
        //arraylist for progress done

        Log.d(TAG, String.valueOf(durationListMonthArrayListSchedule.size()));
        Log.d(TAG, String.valueOf(durationListMonthArrayListProgress.size()));

        //creating chartSchedule and chartProgress settings
        setChartSettings(chartSchedule, chartProgress);

        //chart settign done. Need to add the width later

        Log.d(TAG, "AFte rsetting settings of chart");

        //   float barWidth = 0.4f; // Change this value as per your requirement

        //set date on xAxis along with setting the padding from sides
        //the width is set later

        setXAxisLabelsForYear(chartSchedule,durationListMonthArrayListSchedule);
        setXAxisLabelsForYear(chartProgress,durationListMonthArrayListProgress);

        Log.d(TAG, String.valueOf(durationListMonthArrayListSchedule.size()) + "After Date setting on X Axis");
        Log.d(TAG, String.valueOf(durationListMonthArrayListProgress.size()) + "After Date setting on X Axis");


        //setting color and values
        //data needs to be compatabile with the barChart
        //can be float or int
        //customize valueFormatting with valueFormatter to chagne into toehr types
        Log.d(TAG, "Before barEntries");
        Log.d(TAG, durationListMonthArrayListSchedule.size()+" ");
        Log.d(TAG, durationListMonthArrayListProgress.size()+" ");

        //enter barEntries
        //put entries in format in according to workType
        //wrote by error/?  setBarEntries(durationListArrayListSchedule);
        List<BarEntry> barEntriesScheduleChart =  setBarEntriesForYear(durationListMonthArrayListSchedule);
        List<BarEntry> barEntriesProgressChart =  setBarEntriesForYear(durationListMonthArrayListProgress);


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

    public void setXAxisLabels(BarChart barChart, ArrayList<DurationList> durationListArrayList){
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
                if (index >= 0 && index < durationListArrayList.size()) {
                    // Get the corresponding date from the dates array
                    LocalDate date = durationListArrayList.get(index).getDate();
                    Log.d(TAG, "Called Date" +durationListArrayList.get(index).getDate());
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
        xAxis.setAxisMaximum(durationListArrayList.size() - 1 + paddingSpace);
    }

    public void setXAxisLabelsForYear(BarChart barChart, ArrayList<DurationListMonth> durationListArrayList){
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
                if (index >= 0 && index < durationListArrayList.size()) {
                    // Get the corresponding date from the dates array\
                    //returing int instead of month
                    int monthValue = durationListArrayList.get(index).getMonth();
                    int year = 2023; // Replace with the actual year value
                    int dayOfMonth = 1; // Replace with the actual day of the month value

                    LocalDate date = LocalDate.of(year, monthValue, dayOfMonth);

                    Log.d(TAG, "Called Date" +durationListArrayList.get(index).getMonth());
                    Log.d(TAG, "Called Date" +LocalDate.ofEpochDay(durationListArrayList.get(index).getMonth()));

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
        xAxis.setAxisMaximum(durationListArrayList.size() - 1 + paddingSpace);
    }

    private List<BarEntry> setBarEntries(ArrayList <DurationList> durationListArrayList){
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < durationListArrayList.size(); i++) {
            DurationList localDurationList = durationListArrayList.get(i);
            barEntries.add(new BarEntry(i, new float[]{localDurationList.getDurationList().get(0).getDuration(), localDurationList.getDurationList().get(1).getDuration(), localDurationList.getDurationList().get(2).getDuration(), localDurationList.getDurationList().get(3).getDuration(), localDurationList.getDurationList().get(4).getDuration(), localDurationList.getDurationList().get(5).getDuration()}));
            Log.d(TAG+"1", localDurationList.getDurationList().get(0).getDuration() +" " + localDurationList.getDurationList().get(1).getDuration()+" "+ localDurationList.getDurationList().get(2).getDuration() + " " + localDurationList.getDurationList().get(3).getDuration() + " " + localDurationList.getDurationList().get(4).getDuration() + " " + localDurationList.getDurationList().get(5).getDuration()+" ");

        }
        return barEntries;
    }

    private List<BarEntry> setBarEntriesForYear(ArrayList <DurationListMonth> durationListArrayList){
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < durationListArrayList.size(); i++) {
            DurationListMonth localDurationList = durationListArrayList.get(i);
            barEntries.add(new BarEntry(i, new float[]{localDurationList.getDurationList().get(0).getDuration(), localDurationList.getDurationList().get(1).getDuration(), localDurationList.getDurationList().get(2).getDuration(), localDurationList.getDurationList().get(3).getDuration(), localDurationList.getDurationList().get(4).getDuration(), localDurationList.getDurationList().get(5).getDuration()}));
            Log.d(TAG+"1", localDurationList.getDurationList().get(0).getDuration() +" " + localDurationList.getDurationList().get(1).getDuration()+" "+ localDurationList.getDurationList().get(2).getDuration() + " " + localDurationList.getDurationList().get(3).getDuration() + " " + localDurationList.getDurationList().get(4).getDuration() + " " + localDurationList.getDurationList().get(5).getDuration()+" ");
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