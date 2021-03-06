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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;


public class addTask extends Fragment {

    private EditText taskNameInput;
    private EditText assigneeInput;
    private Button cancelBtn;
    private Button createBtn;
    private View root;
    private SharedPreferences mPrefs;
    private SharedPreferences sPrefs;
    private SharedPreferences myPrefs;
    private DatePicker datePicker;
    public addTask() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPrefs = getContext().getSharedPreferences("TaskObjects1", Activity.MODE_PRIVATE);
        sPrefs = getContext().getSharedPreferences("Settings Pref", Activity.MODE_PRIVATE);
        myPrefs = getContext().getSharedPreferences("TaskObjects2", Activity.MODE_PRIVATE);
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

                if (taskName.equals("")) {
                    Toast.makeText(getContext(), "Please Enter a Task", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (assignee.equals("")) {
                    assignee = "tbd";
                }
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();
                int today_year = Calendar.getInstance().get(Calendar.YEAR);
                int today_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int today_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                if(year < today_year || (year == today_year && month < today_month)
                        || (year == today_year && month < today_month && day < today_day)) {
                    Toast.makeText(getContext(), "Please Pick a Future Date", Toast.LENGTH_SHORT).show();
                        return;
                }
                Task task = new Task(assignee, taskName, month, day, year);
                ArrayList<Task> taskList;
                taskList = getTaskList();
                taskList.add(task);
                ArrayList<Task> myTaskList = getMyTaskList();
                String check = task.getAssigned() + '!';
                if (check.equalsIgnoreCase(sPrefs.getString("username", ""))) {
                    myTaskList.add(task);
                }
                //System.out.println(" TaskList : " + taskList);

                //faster sort only at end and not at insert?
                Collections.sort(taskList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getDateSeq().compareTo(o2.getDateSeq());
                    }
                });
                Collections.sort(myTaskList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getDateSeq().compareTo(o2.getDateSeq());
                    }
                });

                storeTaskList(taskList);
                storeMyTaskList(myTaskList);


                //updateNavigationHeader();

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new addTask());
                trans.commit();
                manager.popBackStack();

            }
        });
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        TextView taskToDoNum = header.findViewById(R.id.taskToDoNum);
        ArrayList<Task> taskList = getMyTaskList();
        int length = taskList.size();
        String numString = length + " Tasks To Do";
        taskToDoNum.setText(numString);
        return root;
    }

    public void updateNavigationHeader(){
        SharedPreferences settings_pref = getActivity().getSharedPreferences("TaskObjects2", Activity.MODE_PRIVATE);
        NavigationView navigationView = (NavigationView) root.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.myName);
        TextView taskToDoNum = header.findViewById(R.id.taskToDoNum);
        ArrayList<Task> taskList = getMyTaskList();
        int length = taskList.size();
        String numString = length + " Tasks To Do";
        taskToDoNum.setText(numString);
        name.setText(settings_pref.getString("username", "MyName!"));

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
        ArrayList<Task> myTaskList= new ArrayList<Task>();
        Gson gson = new Gson();
        String json = myPrefs.getString("TaskObjects2", "empty");
        if(json.equals("empty")){
            return myTaskList;
        }
        Type type = new TypeToken<List<Task>>(){}.getType();
        myTaskList = gson.fromJson(json, type);
        return myTaskList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        TextView taskToDoNum = header.findViewById(R.id.taskToDoNum);
        ArrayList<Task> taskList = getMyTaskList();
        int length = taskList.size();
        String numString = length + " Tasks To Do";
        taskToDoNum.setText(numString);
    }

    public void storeMyTaskList(ArrayList<Task> taskList){
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefsEditor.putString("TaskObjects2", json);
        prefsEditor.commit();
    }
}
