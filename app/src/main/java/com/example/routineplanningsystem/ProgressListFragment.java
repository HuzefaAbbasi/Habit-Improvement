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
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Date: " + date1.toString());
        Log.d(TAG, "onCreate: ProgressListFragment created");


        DBHelper habit = new DBHelper(getActivity(), "habit",null, 1);

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