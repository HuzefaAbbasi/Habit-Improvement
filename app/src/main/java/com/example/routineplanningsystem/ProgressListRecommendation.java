package com.example.routineplanningsystem;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProgressListRecommendation extends AppCompatActivity {
    private static final String TAG = "ProgressListFragment";
    private RecyclerView recyclerView;
    private TextView textView;

    private ProgressRecyclerViewRecommendation progressRecyclerViewRecommendation;
    private ArrayList<Progress> progressArrayList;
    private ArrayAdapter<String> arrayAdapter;
//may be more optimal if separeate calss created

    //    private Button buttonTest;
    private LocalDate date1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_list);
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);
        textView.setText("Recommended");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        UserManager userManager = UserManager.getInstance();
        String userName = userManager.getUserName();

        Toast.makeText(this, userName +"! You are making good progress", Toast.LENGTH_SHORT).show();
        DBHelper habit = new DBHelper(this, "habit",null, 1);
        Log.d(TAG, "onCreateView: BeforeFunction ");

        date1 = habit.getOptimalDate("Month");
        Log.d(TAG, "onCreateView: Date: " + date1.toString());


        //for insertion of task list into taskArrayList
        progressArrayList = new ArrayList<>();
        List<Progress> progressList = habit.getAllProgressForDate(date1);
        for(Progress progress : progressList) {
            progressArrayList.add(progress);
        }
        Log.d("ProgressListRecommendation", String.valueOf(progressArrayList.size()));
        //for the recycleViewAdapter
        progressRecyclerViewRecommendation = new ProgressRecyclerViewRecommendation(this,progressArrayList, habit);
        recyclerView.setAdapter(progressRecyclerViewRecommendation);
        // Inflate the layout for

    }

}
