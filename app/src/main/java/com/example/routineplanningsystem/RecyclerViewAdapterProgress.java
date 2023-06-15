package com.example.routineplanningsystem;



import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RecyclerViewAdapterProgress extends RecyclerView.Adapter<RecyclerViewAdapterProgress.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterProgress";
    private Context context;
    private List<Progress> progressList;
    private DBHelper habit;

    public RecyclerViewAdapterProgress(Context context, List<Progress> progressList, DBHelper habit) {
        this.context = context;
        this.progressList = progressList;
        this.habit = habit;
    }

    public void removeItem(int position) {
        // Remove the item from the list
        progressList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    //where to get the single object/card
    public RecyclerViewAdapterProgress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_progress,parent,false);
        Log.d(TAG, "onCreate: ViewHolder created");
        return new ViewHolder(view);
    }
    //what will happen with the ViewHolderObject
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterProgress.ViewHolder holder, int position) {
        Progress progress = progressList.get(position);
        // Testing s       Toast.makeText(context, String.valueOf(task.getTaskType()), Toast.LENGTH_SHORT).show();
        LocalTime startTime = progress.getStartTime();

// Convert start time and end time to milliseconds since midnight

// Calculate the duration in milliseconds
        long durationInMillis = calculateDurationInMillis(progress);

// Convert the duration to the desired format (e.g., hours and minutes)
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60;
        String durationString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        durationString = durationString + " mins";
        //for duration caluclation, let's see
        String amPm;
       LocalTime startTimeAMPM = progress.getStartTime();
       int hourOfDay = startTimeAMPM.getHour();
       int hour;
       int mins = startTimeAMPM.getMinute();
        if (hourOfDay >= 12) {
            amPm = "PM";
            hour = (hourOfDay == 12) ? 12 : hourOfDay - 12;
        } else {
            amPm = "AM";
            hour = (hourOfDay == 0) ? 12 : hourOfDay;
        }

        startTimeAMPM = LocalTime.of(hour, mins);

        holder.duration.setText(durationString);
        holder.taskName.setText(progress.getTask().getTaskName());
        holder.startingTime.setText(startTimeAMPM.toString() + " "+ amPm);


        Log.d(TAG, "onBindViewHolder: Progress: " + progress.getTask().getTaskName()); // Add this log to check the progress details
        Log.d(TAG, "onBindViewHolder: Position: " + position); // Add this log to check the current position
        Log.d(TAG, "onCreate: RecycleViewHolder Exectured Fully");

    }



    //for multiple usages
    private long calculateDurationInMillis(Progress progress) {
        LocalTime startTime = progress.getStartTime();
        LocalTime endTime = progress.getEndTime();

        long startTimeInMillis = startTime.toNanoOfDay() / 1_000_000;
        long endTimeInMillis = endTime.toNanoOfDay() / 1_000_000;

        return endTimeInMillis - startTimeInMillis;
    }

    //how many items
    @Override
    public int getItemCount() {

        Log.d("view size", String.valueOf(progressList.size()));
        return progressList.size();
    }

    //need this class //used after the first card created
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView taskName;
        public TextView duration;
        public TextView startingTime;
        public CountDownTimer countDownTimer;
        public TextView countDownTimerText;
        public Button deleteButton;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            taskName = itemView.findViewById(R.id.textView7);
            duration = itemView.findViewById(R.id.textView8);
            startingTime = itemView.findViewById(R.id.textView9);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    // Handle the delete button click event for the specific card here
                    // Remove the item from the list and notify the adapter
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        // Delete the corresponding Progress entry from the database using the position or Progress ID
                        Progress progress = progressList.get(position);
                        LocalDate localDate = progress.getDate();
                        LocalTime localStartTime = progress.getStartTime();
                        LocalTime localEndTime = progress.getEndTime();
                        habit.deleteProgress(localDate,localStartTime,localEndTime);
                        //for updation in real time
                        progressList.remove(position);
                        notifyItemRemoved(position);

                        Log.d(TAG, "Succesffuly deleted from RecyclerView");
                        Toast.makeText(itemView.getContext() ,"Deletion Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            // Handle the long click event
            // Handle the long click event for the card here
            // Show the delete button when long clicked
            deleteButton.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onClick(View v) {
            deleteButton.setVisibility(View.INVISIBLE);
        }

    }

}
