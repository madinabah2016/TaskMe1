package com.example.taskme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class view_tasks extends AppCompatActivity {
    private Button completeBtn;
    private TextView titleView;
    private TextView deadlineView;
    private TextView assigneeView;
    private SharedPreferences mPrefs;
    private SharedPreferences myPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        mPrefs = getSharedPreferences("TaskObjects1", Activity.MODE_PRIVATE);
        myPrefs = getSharedPreferences("TaskObjects2", Activity.MODE_PRIVATE);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        completeBtn = findViewById(R.id.completeBtn);
        titleView = findViewById(R.id.taskDisplay);
        assigneeView = findViewById(R.id.assigneeDisplay);
        deadlineView = findViewById(R.id.deadline);

        Bundle bundle = getIntent().getExtras();
        titleView.setText(bundle.getString("Task Name", ""));
        assigneeView.setText(bundle.getString("Assignee Name", ""));
        deadlineView.setText(bundle.getString("Deadline", ""));

        final int position = bundle.getInt("Position", 0);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences specialPref = getSharedPreferences("Special", Activity.MODE_PRIVATE);
                boolean isMyTask = specialPref.getBoolean("Flag", false);

                ArrayList<Task> taskList = getTaskList();
                ArrayList<Task> myTaskList = getMyTaskList();
                if(!isMyTask) {
                    for (int i = 0; i < myTaskList.size(); i++) {
                        if (myTaskList.get(i).toString().equals(taskList.get(position).toString())) {
                            myTaskList.remove(i);
                            storeMyTaskList(myTaskList);
                            System.out.println("Hello From View");
                        }
                    }
                    taskList.remove(position);
                    storeTaskList(taskList);
                }
                else{
                    for (int i = 0; i < taskList.size(); i++) {
                        if (taskList.get(i).toString().equals(myTaskList.get(position).toString())) {
                            taskList.remove(i);
                            storeTaskList(taskList);
                            System.out.println("Hello From View");
                        }
                    }
                    myTaskList.remove(position);
                    storeMyTaskList(myTaskList);
                }


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    public ArrayList<Task> getTaskList(){

        ArrayList<Task> taskList= new ArrayList<Task>();
        Gson gson = new Gson();
        String json = mPrefs.getString("TaskObjects1", "empty");
        if(json.equals("empty")){
            return taskList;
        }
        Type type = new TypeToken<List<Task>>(){}.getType();
        taskList = gson.fromJson(json, type);

        return taskList;
    }

    public void storeTaskList(ArrayList<Task> taskList){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefsEditor.putString("TaskObjects1", json);
        prefsEditor.commit();

    }

    public ArrayList<Task> getMyTaskList(){

        ArrayList<Task> taskList= new ArrayList<Task>();
        Gson gson = new Gson();
        String json = myPrefs.getString("TaskObjects2", "empty");
        if(json.equals("empty")){
            return taskList;
        }
        Type type = new TypeToken<List<Task>>(){}.getType();
        taskList = gson.fromJson(json, type);

        return taskList;
    }

    public void storeMyTaskList(ArrayList<Task> taskList){
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefsEditor.putString("TaskObjects2", json);
        prefsEditor.commit();

    }
}
