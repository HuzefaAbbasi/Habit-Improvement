package com.example.stackedbarchart;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

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

        BarChart chartSchedule = findViewById(R.id.chartSchedule);
        BarChart chartProgress = findViewById(R.id.chartProgress);
        Button buttonWeek = findViewById(R.id.buttonByWeek);
        Button buttonMonth = findViewById(R.id.buttonByMonth);
        Button buttonYear = findViewById(R.id.buttonByYear);
        Button buttonTaskView = findViewById(R.id.buttonTaskView);

//creating arrayList for dates
        Log.d(TAG, "Start succesful");
        //FOr month and week data
        DBHelper habit = new DBHelper(this, "db", null, 1); //check

        Task task1 = new Task("6", null ,2);
        Task task2 = new Task("7", null ,3);


        habit.insertTask(task1);  habit.insertTask(task2);
        LocalTime time = LocalTime.of(12,55);
        LocalTime endtime = LocalTime.of(15,12);

        LocalTime time2 = LocalTime.of(12,56);
        LocalTime endtime2 = LocalTime.of(19,12);

        LocalTime time3 = LocalTime.of(17,57);
        LocalTime endtime3 = LocalTime.of(19,12);

        LocalTime time4 = LocalTime.of(17,24);
        LocalTime endtime4 = LocalTime.of(19,12);

        //for formatting and inserting hte schduele into Database
        LocalDate date = LocalDate.of(2023, 6, 1);
        LocalDate date2 = LocalDate.of(2023, 5, 6);
        LocalDate date3 = LocalDate.of(2023, 5, 30);
        LocalDate date4 = LocalDate.of(2023, 5, 28);


        //        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate1 = LocalDate.parse(date.toString(), formatterDate);
//        LocalDate localDate2 = LocalDate.parse(date2.toString(), formatterDate);
//
//        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
//        LocalTime localStartTime = LocalTime.parse(time.toString(), formatterTime);
//        LocalTime localEndTime = LocalTime.parse(endtime.toString(), formatterTime);
//
//        LocalTime localStartTime2 = LocalTime.parse(time2.toString(), formatterTime);
//        LocalTime localEndTime2 = LocalTime.parse(endtime2.toString(), formatterTime);


        //   Schedule schedule = new Schedule();
//        Schedule schedule1 = new Schedule(localDate1, localStartTime, localEndTime, task1);
//        Schedule schedule2 = new Schedule(localDate2, localStartTime2, localEndTime2, task2);
        Schedule schedule1 = new Schedule(date, time, endtime, task1);
        Schedule schedule2 = new Schedule(date2, time2, endtime2, task2);
        Schedule schedule3 = new Schedule(date3, time3, endtime3, task1);
        Schedule schedule4 = new Schedule(date3, time4, endtime4, task2);

        habit.insertSchedule(schedule1); habit.insertSchedule(schedule2);
        habit.insertSchedule(schedule3); habit.insertSchedule(schedule4);

        chartsByWeekAndMonth(chartSchedule,chartProgress,"Week");

        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByWeekAndMonth(chartSchedule,chartProgress,"Week");

            }
        });

        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByWeekAndMonth(chartSchedule,chartProgress,"Month");
            }
        });

        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartsByYear(chartSchedule,chartProgress);
            }
        });
//for transition between by task and full view in report
        buttonTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //need dates
        //creating database instance

        Log.d(TAG, "Final in Main succesful");
    }

    private void chartsByWeekAndMonth(BarChart chartSchedule, BarChart chartProgress, String weekOrMonth){
        DBHelper habit = new DBHelper(this, "db", null, 1); //check

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
        DBHelper habit = new DBHelper(this, "db", null, 1); //check

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
        chartSchedule.setBackgroundColor(Color.WHITE);

        chartProgress.clear(); //for clearing existing data from chartProgress
        chartProgress.setDrawBarShadow(false);
        chartProgress.setDrawValueAboveBar(true);
        chartProgress.getDescription().setEnabled(false);
        chartProgress.setBackgroundColor(Color.WHITE);
    }

    public void setXAxisLabels(BarChart barChart, ArrayList<DurationList> durationListArrayList){
        XAxis xAxis = barChart.getXAxis();
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
        BarDataSet dataSet = new BarDataSet(barEntries, "Report");
// Set color for each entry based on its category
        dataSet.setColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY, Color.RED, Color.BLACK); //doesn't recognize border it seems
        dataSet.setStackLabels(new String[]{"Label 1", "Label 2", "Label 3", "Label 4", "Label 5", "Label 6"});

        //setting chartChart to bardata
        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);
        // data.groupBars(0,1,2);
        barChart.setData(data);
        barChart.invalidate(); //additonal function for redrawal of graph with the new datait seems
        barChart.animateY(1000);
    }


    //method for color to entreis
//    private int getColorForCategory(int category) {
//        int color;
//
//        // Define color mapping based on category value
//        switch (category) {
//            case 1:
//                color = Color.RED;//0-ikkl.kl,k.
//                break;
//            case 2: color = Color.GREEN;
//                break;
//            case 3:
//                color = Color.BLUE;
//                break;
//            // Add more cases for additional categories if needed
//            default:
//                color = Color.GRAY;
//                break; // Set a default color if category value doesn't match any specific case
//        }
//        Log.d("Color", "Category: " + category + ", Color: " + color);
//
//        return color;
//    }



}