package com.example.routineplanningsystem;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
  private Context context;
  private List<Task> taskList;

    public RecyclerViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    //where to get the single object/card
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskrow,parent,false);
         return new ViewHolder(view);
    }
//what will happen with the ViewHolderObject
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
       Task task = taskList.get(position);
 // Testing ovalBox       Toast.makeText(context, String.valueOf(task.getTaskType()), Toast.LENGTH_SHORT).show();

        holder.taskName.setText(task.getTaskName());
             if(task.getTaskType() == 1)
                 holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button);
             else if(task.getTaskType() == 2)
                holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button2);
             else if(task.getTaskType() == 3)
                holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button3);
             else if(task.getTaskType() == 4)
                holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button4);
             else if(task.getTaskType() == 5)
                holder.viewOfTaskList.setBackgroundResource(R.drawable.circular_button5);
             else if(task.getTaskType() == 6)
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
          public TextView taskName;
          public View viewOfTaskList;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

            taskName = itemView.findViewById(R.id.textView);
            viewOfTaskList = itemView.findViewById(R.id.viewOfTaskList);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
