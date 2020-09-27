package com.appstone.keepnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskInsertActivity extends AppCompatActivity {
    private EditText mEtTaskTitle;
    private LinearLayout mEtInsertTasks;
    private LinearLayout mEtAddListItem;
    private Button mBtnAddTask;
    private DBhelper dBhelper;
    private int taskID = 0;
    private int taskDetailID;
    private ArrayList<TaskItem> taskItemList;
    private boolean isEdited;
    public static final String BUNDLE_IS_UPDATE = "is_edited";
    public static final String BUNDLE_TASK = "task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_insert);
        mEtAddListItem = findViewById(R.id.ll_add_item);
        mBtnAddTask = findViewById(R.id.btn_enter_data);

        mEtTaskTitle = findViewById(R.id.et_task_title);
        mEtInsertTasks = findViewById(R.id.ll_dynamic_task);
        mEtAddListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTaskItem();
            }
        });
        mBtnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTaskEnterClicked();
            }
        });
        taskItemList = new ArrayList<>();
        dBhelper = new DBhelper(TaskInsertActivity.this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            isEdited = data.getBoolean(BUNDLE_IS_UPDATE);
            TaskDetail updateDetail = (TaskDetail) data.getSerializable(BUNDLE_TASK);

            taskDetailID = updateDetail.id;
            mEtTaskTitle.setText(updateDetail.taskTitle);

            if (updateDetail != null) {
                ArrayList<TaskItem> taskItems = TaskItem.convertTaskStringToList(updateDetail.taskItemArrayValue);
                for (int i = 0; i < taskItems.size(); i++) {
                    final TaskItem itemValue = taskItems.get(i);
                    View taskView = LayoutInflater.from(TaskInsertActivity.this).inflate(R.layout.cell_insert_item, null);
                    final EditText mEtTaskItem = taskView.findViewById(R.id.et_task_item);
                    final ImageView mIvTaskDone = taskView.findViewById(R.id.iv_task_done);
                    mIvTaskDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //           TaskItem taskItem = new TaskItem();
                            //         taskItem.taskID = itemValue.taskID;
                            //        taskItem.taskItem = mEtTaskItem.getText().toString();
                            for (TaskItem task : taskItemList) {
                                if (task.taskID == itemValue.taskID) {
                                    task.taskItem = mEtTaskItem.getText().toString();
                                }

                            }

                            mEtAddListItem.setEnabled(true);
                            mEtAddListItem.setAlpha(1.0f);
                            mBtnAddTask.setEnabled(true);
                            mBtnAddTask.setAlpha(1.0f);
                            mIvTaskDone.setVisibility(View.GONE);
                        }
                    });
                    mEtTaskItem.setText(itemValue.taskItem);

                    TaskItem updatedTaskItem = new TaskItem();
                    updatedTaskItem.taskID = itemValue.taskID;
                    updatedTaskItem.taskItem = itemValue.taskItem;
                    updatedTaskItem.isTaskDone = itemValue.isTaskDone;
                    taskItemList.add(updatedTaskItem);
                    taskID++;
                    mEtInsertTasks.addView(taskView);
                }
            }
            if (isEdited) {
                mBtnAddTask.setText("Update Task");
            }
        }
    }

    private void insertTaskItem() {
        taskID++;
        mEtAddListItem.setEnabled(false);
        mEtAddListItem.setAlpha(0.5f);
        mBtnAddTask.setEnabled(false);
        mBtnAddTask.setAlpha(0.5f);

        View taskView = LayoutInflater.from(TaskInsertActivity.this).inflate(R.layout.cell_insert_item, null);
        final EditText mEtTaskItem = taskView.findViewById(R.id.et_task_item);
        final ImageView mIvTaskDone = taskView.findViewById(R.id.iv_task_done);

        mIvTaskDone.setVisibility(View.GONE);

        mEtTaskItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    mIvTaskDone.setVisibility(View.VISIBLE);
                } else {
                    mIvTaskDone.setVisibility(View.GONE);
                }
            }
        });
        mIvTaskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItem taskItem = new TaskItem();
                taskItem.taskID = taskID;
                taskItem.taskItem = mEtTaskItem.getText().toString();
                taskItemList.add(taskItem);
                mEtAddListItem.setEnabled(true);
                mEtAddListItem.setAlpha(1.0f);
                mBtnAddTask.setEnabled(true);
                mBtnAddTask.setAlpha(1.0f);
                mIvTaskDone.setVisibility(View.GONE);
            }
        });
        mEtInsertTasks.addView(taskView);
    }

    private void onTaskEnterClicked() {
        String taskTitle = mEtTaskTitle.getText().toString();
        if (taskTitle.isEmpty() || taskItemList.size() == 0) {
            Toast.makeText(TaskInsertActivity.this, "Title or item is Empty", Toast.LENGTH_LONG).show();
            return;
        }

        String itemArray = TaskItem.convertTaskListToString(taskItemList);
        if (!isEdited) {
            dBhelper.insetDataToDatabase(dBhelper.getWritableDatabase(), taskTitle, itemArray);
        } else {
            dBhelper.updateDataToDatabase(dBhelper.getWritableDatabase(), taskTitle, itemArray, taskDetailID);
        }
        setResult(Activity.RESULT_OK);
        finish();
    }

}