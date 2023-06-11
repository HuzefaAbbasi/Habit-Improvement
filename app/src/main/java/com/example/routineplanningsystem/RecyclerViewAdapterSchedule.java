package com.example.routineplanningsystem;



import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RecyclerViewAdapterSchedule extends RecyclerView.Adapter<RecyclerViewAdapterSchedule.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterSchedule";
    private Context context;
    private List<Schedule> scheduleList;
    private DBHelper habit;
    private CountDownTimer currentCountDownTimer;

    public RecyclerViewAdapterSchedule(Context context, List<Schedule> scheduleList, DBHelper habit) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.habit = habit;
    }

    public void removeItem(int position) {
        // Remove the item from the list
        scheduleList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    //where to get the single object/card
    public RecyclerViewAdapterSchedule.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_schedule,parent,false);
        Log.d(TAG, "onCreate: ViewHolder created");
        return new ViewHolder(view);
    }
    //what will happen with the ViewHolderObject
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSchedule.ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        // Testing s       Toast.makeText(context, String.valueOf(task.getTaskType()), Toast.LENGTH_SHORT).show();
        LocalTime startTime = schedule.getStartTime();

// Convert start time and end time to milliseconds since midnight

// Calculate the duration in milliseconds
        long durationInMillis = calculateDurationInMillis(schedule);

// Convert the duration to the desired format (e.g., hours and minutes)
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60;
        String durationString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        //for duration caluclation, let's see
        holder.duration.setText(durationString);
        holder.taskName.setText(schedule.getTask().getTaskName());
        holder.startingTime.setText(schedule.getStartTime().toString());
        holder.countDownTimerText.setText(schedule.getEndTime().toString());


        //        if(task.getTaskType() == 1)
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button);
//        else if(task.getTaskType() == 2)
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button2);
//        else if(task.getTaskType() == 3)

//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button3);
//        else if(task.getTaskType() == 4)
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button4);
//        else if(task.getTaskType() == 5)
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button5);
//        else if(task.getTaskType() == 6)
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button6);
//            //this last line is DUMMY LINE
//        else {
//            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button4);
//            //      Toast.makeText(this, "Demo", Toast.LENGTH_SHORT).show();
//        }

        Log.d(TAG, "onBindViewHolder: Schedule: " + schedule.getTask().getTaskName()); // Add this log to check the schedule details
        Log.d(TAG, "onBindViewHolder: Position: " + position); // Add this log to check the current position
        Log.d(TAG, "onCreate: RecycleViewHolder Exectured Fully");
// Start the countdown timer for the appropriate card
//       commented until further usage need
//        if (currentCountDownTimer != null) {
//            currentCountDownTimer.cancel();
//        }
        if (shouldStartCountDown(schedule)) {
            holder.countDownTimerText.setVisibility(View.VISIBLE);
            startCountDown(holder, durationInMillis);
        } else {
            holder.countDownTimerText.setVisibility(View.INVISIBLE);
            holder.countDownTimerText.setText("");
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
                holder.countDownTimer = null;
            }
        }
        Log.d(TAG, "onCreate: Starting CountDown");

        //       holder.taskColor.setColor();
    }

    private boolean shouldStartCountDown(Schedule schedule) {
        Log.d(TAG, " Build.VERSION_CODES.O Found)");
        LocalTime currentTime = LocalTime.now();
        //removed the :ss format becasue comparison can't happen
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeStr = currentTime.format(timeFormatter);
        Log.d(TAG, "ShouldCountDownStart?" + startTimeStr.equals(schedule.getStartTime().toString()));
        Log.d(TAG, "ShouldCountDownStart?" + startTimeStr + schedule.getStartTime().toString());
        Log.d(TAG, "ShouldCountDownStart?" + startTimeStr.equals(schedule.getStartTime().toString()));

        return startTimeStr.equals(schedule.getStartTime().toString());
    }

    private void startCountDown(ViewHolder holder, long durationInMillis) {
        Log.d(TAG, "startCountDown: Starting countdown"); // Add this log to check if the countdown starts correctly
        currentCountDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                String countdownString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                holder.countDownTimerText.setText(countdownString);
            }

            @Override
            public void onFinish() {
                holder.countDownTimerText.setText("00:00");
                holder.countDownTimerText.setVisibility(View.INVISIBLE);

                // Trigger the countdown for the next card if available
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < scheduleList.size() - 1) {
                    Schedule nextSchedule = scheduleList.get(currentPosition + 1);
                    long nextDurationInMillis = calculateDurationInMillis(nextSchedule);
                    startCountDown(holder, nextDurationInMillis);
                }
            }
        }.start();
    }
    //for multiple usages
    private long calculateDurationInMillis(Schedule schedule) {
        LocalTime startTime = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();

        long startTimeInMillis = startTime.toNanoOfDay() / 1_000_000;
        long endTimeInMillis = endTime.toNanoOfDay() / 1_000_000;

        return endTimeInMillis - startTimeInMillis;
    }

    //how many items
    @Override
    public int getItemCount() {

        Log.d("view size", String.valueOf(scheduleList.size()));
        return scheduleList.size();
    }

    //need this class //used after the first card created
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView taskName;
        public TextView duration;
        public TextView startingTime;
 public CountDownTimer countDownTimer;
 public TextView countDownTimerText;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            taskName = itemView.findViewById(R.id.textView7);
            duration = itemView.findViewById(R.id.textView8);
            startingTime = itemView.findViewById(R.id.textView9);
            countDownTimerText = itemView.findViewById(R.id.textView10);

        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
           // DBHelper habit = new DBHelper(context.getApplicationContext(), "db", null,1);
            // Handle the long click event
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Call the removeItem() method from the adapter to delete the card
//                ((RecyclerViewAdapterSchedule) itemView.getContext()).removeItem(position);
                // Delete the corresponding Schedule entry from the database using the position or Schedule ID
                Schedule schedule = scheduleList.get(position);
                LocalDate localDate = schedule.getDate();
                LocalTime localStartTime = schedule.getStartTime();
                LocalTime localEndTime = schedule.getEndTime();
                habit.deleteSchedule(localDate,localStartTime,localEndTime);
              //for updation in real time
                scheduleList.remove(position);
                notifyItemRemoved(position);

                Log.d(TAG, "Succesffuly deleted from RecyclerView");
                Toast.makeText(itemView.getContext() ,"Deletion Successful", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }

}
