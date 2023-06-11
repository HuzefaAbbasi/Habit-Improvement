package com.example.routineplanningsystem;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListFragment extends Fragment {
    private static final String TAG = "ScheduleListFragment";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSchedule recyclerViewAdapterSchedule;
    private ArrayList<Schedule> scheduleArrayList;
    private ArrayAdapter<String> arrayAdapter;
//may be more optimal if separeate calss created

    //    private Button buttonTest;
private LocalDate date1;  //for displaying changing data in tabs

    //for passing dates to tab
    public static ScheduleListFragment newInstance(LocalDate date) {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date1 = (LocalDate) getArguments().getSerializable("date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.schedule_list,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        //buttonTest = view.findViewById(R.id.buttonTest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Date: " + date1.toString());
        Log.d(TAG, "onCreate: ScheduleListFragment created");

        //to pass the date as argument in tabs
//        Bundle args = getArguments();
//        if (args != null && args.containsKey("date")) {
//            date1 = (LocalDate) args.getSerializable("date");
//        } else {
//            // Set a default date if no date is provided
//            date1 = LocalDate.now();
//        }

        DBHelper habit = new DBHelper(getActivity(), "db",null, 1);
        // Test for taskListGetting   Task task1 = new Task("1", null ,2) //Task task2 = new Task("2", null ,2) habit.insertTask(task1);  habit.insertTask(task2);
        Task task1 = new Task("11", null ,1);
        Task task2 = new Task("12", null ,1);


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
        LocalDate date = LocalDate.of(2023, 6, 5);
        LocalDate date2 = LocalDate.of(2023, 6, 5);
        LocalDate date3 = LocalDate.of(2023, 6, 5);
        LocalDate date4 = LocalDate.of(2023, 6, 5);


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

        //     schedule.insertSchedule(task1, localDate1, localStartTime , localEndTime);
        //  schedule.insertSchedule(task2, localDate2, localStartTime2, localEndTime2);

        //for insertion of task list into taskArrayList
        scheduleArrayList = new ArrayList<>();
        List<Schedule> scheduleList = habit.getAllScheduleForDate(date1);
        for(Schedule schedule : scheduleList) {
            scheduleArrayList.add(schedule);
        }
        Log.d("view list", String.valueOf(scheduleArrayList.size()));

//for the recycleViewAdapter
        recyclerViewAdapterSchedule = new RecyclerViewAdapterSchedule(getActivity(),scheduleArrayList, habit);
        recyclerView.setAdapter(recyclerViewAdapterSchedule);
        // Inflate the layout for this fragment
return view;
    }

}