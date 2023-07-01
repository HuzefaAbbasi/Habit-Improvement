package com.example.routineplanningsystem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterTask extends RecyclerView.Adapter<RecyclerViewAdapterTask.ViewHolder> {
    private Context context;
    private List<Task> taskList;
    private DBHelper dbHelper;

    public RecyclerViewAdapterTask(Context context, List<Task> taskList, DBHelper dbHelper) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    //where to get the single object/card
    public RecyclerViewAdapterTask.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskrow, parent, false);
        return new ViewHolder(view);
    }

    //what will happen with the ViewHolderObject
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterTask.ViewHolder holder, int position) {
        Task task = taskList.get(position);
        // Testing ovalBox       Toast.makeText(context, String.valueOf(task.getTaskType()), Toast.LENGTH_SHORT).show();

        holder.taskName.setText(task.getTaskName());
        if (task.getTaskType() == 1)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button);
        else if (task.getTaskType() == 2)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button2);
        else if (task.getTaskType() == 3)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button3);
        else if (task.getTaskType() == 4)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button4);
        else if (task.getTaskType() == 5)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button5);
        else if (task.getTaskType() == 6)
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button6);
            //this last line is DUMMY LINE
        else {
            holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button4);
            //      Toast.makeText(this, "Demo", Toast.LENGTH_SHORT).show();
        }

        //       holder.taskColor.setColor();
    }

    //how many items
    @Override
    public int getItemCount() {
        return taskList.size();
    }


    //need this class //used after the first card created
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {
        public TextView taskName;
        public View viewOfTaskList;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            taskName = itemView.findViewById(R.id.textView);
            viewOfTaskList = itemView.findViewById(R.id.viewOfTaskList);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    // Handle the delete button click event for the specific card here
                    // Remove the item from the list and notify the adapter
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call the removeItem() method from the adapter to delete the card
//                ((RecyclerViewAdapterSchedule) itemView.getContext()).removeItem(position);
                        // Delete the corresponding Schedule entry from the database using the position or Schedule ID
                        Task task = taskList.get(position);
                        String taskName = task.getTaskName();

                        dbHelper.deleteTask(taskName);
                        //for updation in real time
                        taskList.remove(position);
                        notifyItemRemoved(position);

                        Log.d("RecyclerViewAdapterTask", "Succesffuly deleted from RecyclerView");
                        Toast.makeText(itemView.getContext(), "Deletion Successful", Toast.LENGTH_SHORT).show();
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