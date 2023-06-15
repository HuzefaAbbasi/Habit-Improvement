package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgressTabLayOutView extends AppCompatActivity {
    private static final String TAG = "ProgressTabLayOutView";

    //changes for Fragment implemetnation
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapterProgress adapterProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " Build.VERSION_CODES.O Found)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_tablayout_view);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        Button addButton = findViewById(R.id.addButton);
        Button menuTaskButton = findViewById(R.id.menuTaskButton);
        Button menuScheduleButton = findViewById(R.id.menuScheduleButton);
        Button menuReportButton = findViewById(R.id.menuReportButton);
        Log.d(TAG, "onCreate: ProgressTabLayOut created");

        menuTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressTabLayOutView.this, TaskList.class);
                startActivity(intent);
            }
        });

        menuScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressTabLayOutView.this, ScheduleTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressTabLayOutView.this, BarChartClass.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressTabLayOutView.this, AddProgress.class);
                startActivity(intent);
            }
        });


        tabLayout.addTab(tabLayout.newTab().setText("")); // Placeholder tab
        tabLayout.addTab(tabLayout.newTab().setText("")); // Placeholder tab
        tabLayout.addTab(tabLayout.newTab().setText("")); // Placeholder tab
        tabLayout.addTab(tabLayout.newTab().setText("")); // Placeholder tab
        tabLayout.addTab(tabLayout.newTab().setText("")); // Placeholder tab

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabIndicatorFullWidth(false);
        Log.d(TAG, "onCreate: TabLaout made scrollable");


        FragmentManager fragmentManager = getSupportFragmentManager();
        int numberOfDatesToShow = 5; // Adjust this value as per your requirement
        adapterProgress = new ViewPagerAdapterProgress(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapterProgress);

        viewPager2.post(new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(50, false);// Set the center tab as default
            }
        });


        // Set the text on the tabs with the corresponding dates
        for (int i = 0; i < numberOfDatesToShow; i++) {
            LocalDate currentDate = LocalDate.now();
            LocalDate date = currentDate.plusDays(i - 2); // Adjust the offset based on the position

            // Format the date as a string to display on the tab
            String dateString = date.format(DateTimeFormatter.ofPattern("MMM d"));

            tabLayout.getTabAt(i).setText(dateString);
            Log.d(TAG, dateString);

        }
        Log.d(TAG, "onCreate: date set succesful");

//controlling action on tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        // Set up the tab layout with the view pager
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            int offset = adapterProgress.getItemCount() / 2; // Calculate the offset from the center position
            LocalDate currentDate = LocalDate.now();
            LocalDate date = currentDate.plusDays(position - offset); // Adjust the offset based on the position

            String dateString = date.format(DateTimeFormatter.ofPattern("MMM d"));
            tab.setText(dateString);
        }).attach();

        // Log some information for debugging
        Log.d(TAG, "onCreate: Tab count: " + tabLayout.getTabCount());
        Log.d(TAG, "onCreate: View pager item count: " + adapterProgress.getItemCount());
        Log.d(TAG, "onCreate: Current item: " + viewPager2.getCurrentItem());
        Log.d(TAG, "onCreate: ProgressTabLayOut Succesfully Run");

    }
}