package com.appstone.keepnotes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskItem {
    public int taskID;
    public String taskItem;
    public Boolean isTaskDone;

    public static final String TASK_ID = "task_id";
    public static final String TASK_ITEM = "task_item";
    public static final String TASK_IS_DONE = "task_is_done";

    public static String convertTaskListToString(ArrayList<TaskItem> taskItems){
        String itemsArrayValue ="";
        JSONArray jsonArray = new JSONArray();
        for(TaskItem item: taskItems){
            try {
                JSONObject itemObject = new JSONObject();
                itemObject.put(TaskItem.TASK_ID,item.taskID);
                itemObject.put(TaskItem.TASK_ITEM,item.taskItem);
                itemObject.put(TaskItem.TASK_IS_DONE,item.isTaskDone);
                jsonArray.put(itemObject);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
       itemsArrayValue = jsonArray.toString();
        return itemsArrayValue;
    }
    public  static ArrayList<TaskItem> convertTaskStringToList(String taskItemString){
        ArrayList<TaskItem> taskItems = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(taskItemString);
            if(jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jObj = jsonArray.optJSONObject(i);
                    TaskItem item = new TaskItem();
                    item.taskID = jObj.optInt(TaskItem.TASK_ID);
                    item.taskItem = jObj.optString(TaskItem.TASK_ITEM);
                    item.isTaskDone = jObj.optBoolean(TaskItem.TASK_IS_DONE);
                    taskItems.add(item);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return taskItems;
    }
}
