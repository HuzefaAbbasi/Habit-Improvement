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

public class ProgressListFragment extends Fragment {
    private static final String TAG = "ProgressListFragment";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterProgress recyclerViewAdapterProgress;
    private ArrayList<Progress> progressArrayList;
    private ArrayAdapter<String> arrayAdapter;
//may be more optimal if separeate calss created

    //    private Button buttonTest;
    private LocalDate date1;  //for displaying changing data in tabs

    //for passing dates to tab
    public static ProgressListFragment newInstance(LocalDate date) {
        ProgressListFragment fragment = new ProgressListFragment();
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
        View view = inflater.inflate(R.layout.progress_list,container,false);
        recyclerView = view.findViewById(R.id.recyclerView01);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Date: " + date1.toString());
        Log.d(TAG, "onCreate: ProgressListFragment created");


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

      Progress progress1 = new Progress(date, time, endtime, task1, 1,1);
        Progress progress2 = new Progress(date2, time2, endtime2, task2,2,2);
        Progress progress3 = new Progress(date3, time3, endtime3, task1,3,2);
        Progress progress4 = new Progress(date3, time4, endtime4, task2,4,3);

        habit.insertProgress(progress1); habit.insertProgress(progress2);
        habit.insertProgress(progress3); habit.insertProgress(progress4);


        //for insertion of task list into taskArrayList
        progressArrayList = new ArrayList<>();
        List<Progress> progressList = habit.getAllProgressForDate(date1);
        for(Progress progress : progressList) {
            progressArrayList.add(progress);
        }
        Log.d("view list", String.valueOf(progressArrayList.size()));

//for the recycleViewAdapter
        recyclerViewAdapterProgress = new RecyclerViewAdapterProgress(getActivity(),progressArrayList, habit);
        recyclerView.setAdapter(recyclerViewAdapterProgress);
        // Inflate the layout for this fragment
        return view;
    }

}