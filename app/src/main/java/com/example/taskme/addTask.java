package com.example.taskme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class addTask extends Fragment {

    private EditText taskNameInput;
    private EditText assigneeInput;
    private Button cancelBtn;
    private Button createBtn;
    private View root;
    private SharedPreferences mPrefs;
    private DatePicker datePicker;
    public addTask() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPrefs = getContext().getSharedPreferences("TaskObjects1", Activity.MODE_PRIVATE);
        root = inflater.inflate(R.layout.fragment_add_task, container, false);
        taskNameInput = root.findViewById(R.id.taskNameInput);
        assigneeInput = root.findViewById(R.id.assigneeInput);
        cancelBtn = root.findViewById(R.id.cancelbtn);
        createBtn = root.findViewById(R.id.createbtn);
        datePicker = root.findViewById(R.id.datePicker2);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new addTask());
                trans.commit();
                manager.popBackStack();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameInput.getText().toString();
                String assignee = assigneeInput.getText().toString();

                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();

                Task task = new Task(taskName, assignee, 5, month, day, year);

                ArrayList<Task> taskList;
                taskList = getTaskList();
                taskList.add(task);

                System.out.println(" TaskList : " + taskList);

                storeTaskList(taskList);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new addTask());
                trans.commit();
                manager.popBackStack();

            }
        });

        return root;
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
}
