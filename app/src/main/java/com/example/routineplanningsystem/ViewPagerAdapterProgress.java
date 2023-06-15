package com.example.routineplanningsystem;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.time.LocalDate;

public class ViewPagerAdapterProgress extends FragmentStateAdapter {
    private static final String TAG = "ViewPagerAdapterProgress";

    public ViewPagerAdapterProgress(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, " Build.VERSION_CODES.O Found)");

        int offset = getItemCount() / 2; // Calculate the offset from the center position
        LocalDate currentDate = LocalDate.now();
        LocalDate date = currentDate.plusDays(position - offset); // Adjust the offset based on the position

        Log.d(TAG, "createFragment: Position: " + position);
        Log.d(TAG, "createFragment: Date: " + date.toString());
        Log.d(TAG, "onCreate: ViewPagerAdapterProgress created");    return ProgressListFragment.newInstance(date);
    }





    @Override
    public int getItemCount() {
        Log.d(TAG, "onCreate: ItemSize Returned");

        return 100;
    }
}
