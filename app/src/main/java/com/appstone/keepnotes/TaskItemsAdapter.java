package com.appstone.keepnotes;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskItemsAdapter extends RecyclerView.Adapter<TaskItemsAdapter.TaskItemHolder> {
    private Context context;
    private ArrayList<TaskDetail> taskDetails;
    private TaskDoneListener listener;

    public TaskItemsAdapter(Context context, ArrayList<TaskDetail> taskDetails) {
        this.context = context;
        this.taskDetails = taskDetails;
    }

    public void setListener(TaskDoneListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskItemHolder(LayoutInflater.from(context).inflate(R.layout.cell_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemHolder holder, int position) {
        final TaskDetail taskDetail = taskDetails.get(position);
        holder.mTvTaskTitle.setText(taskDetail.taskTitle);

        ArrayList<TaskItem> taskItems = TaskItem.convertTaskStringToList(taskDetail.taskItemArrayValue);
        holder.mLlTaskItem.removeAllViewsInLayout();

        for (int i = 0; i < taskItems.size(); i++) {
            final TaskItem itemValue = taskItems.get(i);
            View taskView = LayoutInflater.from(context).inflate(R.layout.cell_view_item, null);
            TextView mTvTaskItem = taskView.findViewById(R.id.tv_task_item);
            CheckBox mChTask = taskView.findViewById(R.id.ch_taskdone);

            mTvTaskItem.setText(itemValue.taskItem);
            mChTask.setChecked(itemValue.isTaskDone);
            if (itemValue.isTaskDone) {
                mTvTaskItem.setPaintFlags(TextPaint.STRIKE_THRU_TEXT_FLAG);
            }
            mChTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (listener != null) {
                        listener.onTaskItemUpdate(taskDetail, itemValue, b);
                    }
                }
            });
            holder.mLlTaskItem.addView(taskView);

        }
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteClicked(taskDetail);
                }
            }
        });
        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    listener.onEditClicked(taskDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskDetails.size();
    }

    class TaskItemHolder extends RecyclerView.ViewHolder {
        private TextView mTvTaskTitle;
        private LinearLayout mLlTaskItem;

        private ImageView mIvEdit;
        private ImageView mIvDelete;

        public TaskItemHolder(@NonNull View itemView) {
            super(itemView);
            mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            mLlTaskItem = itemView.findViewById(R.id.ll_view_items);
            mIvDelete = itemView.findViewById(R.id.iv_delete);
            mIvEdit = itemView.findViewById(R.id.iv_edit);

        }


    }

    public interface TaskDoneListener {
        void onTaskItemUpdate(TaskDetail taskDetail, TaskItem taskItem, boolean isChecked);

        void onDeleteClicked(TaskDetail taskDetail);

        void onEditClicked(TaskDetail taskDetail);
    }
}
