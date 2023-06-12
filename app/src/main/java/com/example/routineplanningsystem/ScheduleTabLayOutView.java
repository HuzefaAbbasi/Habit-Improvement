package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduleTabLayOutView extends AppCompatActivity {



    private static final String TAG = "MainActivity";

    //changes for Fragment implemetnation
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapterSchedule adapterSchedule;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " Build.VERSION_CODES.O Found)");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.schedule_tablayout_view);
        tabLayout = findViewById(R.id.tab_layout01);
        viewPager2 = findViewById(R.id.view_pager01);
        Log.d(TAG, "onCreate: MainActivity created");

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
        adapterSchedule = new ViewPagerAdapterSchedule(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapterSchedule);

        viewPager2.setCurrentItem(1000); // Set the center tab as default

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
            int offset = adapterSchedule.getItemCount() / 2; // Calculate the offset from the center position
            LocalDate currentDate = LocalDate.now();
            LocalDate date = currentDate.plusDays(position - offset); // Adjust the offset based on the position

            String dateString = date.format(DateTimeFormatter.ofPattern("MMM d"));
            tab.setText(dateString);
        }).attach();

        // Log some information for debugging
        Log.d(TAG, "onCreate: Tab count: " + tabLayout.getTabCount());
        Log.d(TAG, "onCreate: View pager item count: " + adapterSchedule.getItemCount());
        Log.d(TAG, "onCreate: Current item: " + viewPager2.getCurrentItem());
        Log.d(TAG, "onCreate: MainActivity Succesfully Run");

    }
}