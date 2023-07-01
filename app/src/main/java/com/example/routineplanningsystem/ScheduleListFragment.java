package com.example.routineplanningsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListFragment extends Fragment {
    private static final String TAG = "ScheduleListFragment";
    private RecyclerView recyclerView;
    private Button recommendation;
    private RecyclerViewAdapterSchedule recyclerViewAdapterSchedule;
    private ArrayList<Schedule> scheduleArrayList;
    private ArrayAdapter<String> arrayAdapter;
//may be more optimal if separeate calss created

    //    private Button buttonTest;
private LocalDate date1;
//for displaying changing data in tabs

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_list,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recommendation = view.findViewById(R.id.recommendation);
        //buttonTest = view.findViewById(R.id.buttonTest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Date: " + date1.toString());
        Log.d(TAG, "onCreate: ScheduleListFragment created");


        DBHelper dbHelper = new DBHelper(getActivity(), "habit",null, 1);

   //for insertion of task list into taskArrayList
        scheduleArrayList = new ArrayList<>();
        List<Schedule> scheduleList = dbHelper.getAllScheduleForDate(date1);

        for(Schedule schedule : scheduleList) {
            scheduleArrayList.add(schedule);
        }
        Log.d("List Size", "onCreateView: "+scheduleList.size());
        Log.d("view list", String.valueOf(scheduleArrayList.size()));

//for the recycleViewAdapter
        recyclerViewAdapterSchedule = new RecyclerViewAdapterSchedule(getActivity(),scheduleArrayList, dbHelper);
        recyclerView.setAdapter(recyclerViewAdapterSchedule);
        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProgressListRecommendation.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}