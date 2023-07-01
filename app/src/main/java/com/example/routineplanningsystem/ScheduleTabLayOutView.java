package com.example.routineplanningsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduleTabLayOutView extends AppCompatActivity {
    private static final String TAG = "ScheduleTabLayOutView";

    //changes for Fragment implemetnation
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapterSchedule adapterSchedule;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " Build.VERSION_CODES.O Found)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_tablayout_view);


//        //Google Login Code
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this,gso);
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent,1000);
//
//        Log.d(TAG, "Google Name "+ acct.getDisplayName());

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        Button addButton = findViewById(R.id.addButton);
        Button menuProgressButton = findViewById(R.id.menuProgressButton);
        Button menuTaskButton = findViewById(R.id.menuTaskButton);
        Button menuReportButton = findViewById(R.id.menuReportButton);

        menuProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleTabLayOutView.this, ProgressTabLayOutView.class);
                startActivity(intent);
            }
        });

        menuTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleTabLayOutView.this, TaskList.class);
                startActivity(intent);
            }
        });

        menuReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleTabLayOutView.this, BarChartClass.class);
                startActivity(intent);
            }
        });

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
            int offset = adapterSchedule.getItemCount() / 2; // Calculate the offset from the center position
            LocalDate currentDate = LocalDate.now();
            LocalDate date = currentDate.plusDays(position - offset); // Adjust the offset based on the position

            String dateString = date.format(DateTimeFormatter.ofPattern("MMM d"));
            tab.setText(dateString);
        }).attach();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleTabLayOutView.this, AddSchedule.class);
                startActivity(intent);
            }
        });

        // Log some information for debugging
        Log.d(TAG, "onCreate: Tab count: " + tabLayout.getTabCount());
        Log.d(TAG, "onCreate: View pager item count: " + adapterSchedule.getItemCount());
        Log.d(TAG, "onCreate: Current item: " + viewPager2.getCurrentItem());
        Log.d(TAG, "onCreate: MainActivity Succesfully Run");
        UserManager userManager = UserManager.getInstance();
        String userName = userManager.getUserName();
        Log.d("UserName", "Name :"+userName);

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1000){
//            Task<GoogleSignInAccount> myTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                myTask.getResult(ApiException.class);
//            } catch (ApiException e) {
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

}