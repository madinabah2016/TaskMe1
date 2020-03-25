package com.example.taskme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class myTasks extends Fragment {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    private SharedPreferences mPrefs;
    public Context context;
    private Activity activity1;
    private View root;

    public myTasks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Tasks");
        mPrefs = getContext().getSharedPreferences("TaskObjects2", Activity.MODE_PRIVATE);
        context = getContext();
        activity1 = getActivity();


        RecyclerView rv = (RecyclerView) root.findViewById(R.id.myTasks);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<Task> tasks = getMyTaskList();

        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TaskAdapter(tasks, context, activity1);
        rv.setAdapter(mAdapter);


        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Task");
                SharedPreferences specialPref = getContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);
                SharedPreferences.Editor specialPrefEditor = specialPref.edit();
                specialPrefEditor.putBoolean("flag", true);
                specialPrefEditor.commit();

                Fragment addTaskFrag = new addTask();
                transaction.replace(R.id.fragment_container, addTaskFrag);
                transaction.addToBackStack(null);
                transaction.commit();

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



    public ArrayList<Task> getMyTaskList(){
        ArrayList<Task> myTaskList= new ArrayList<Task>();
        Gson gson = new Gson();
        String json = mPrefs.getString("TaskObjects2", "empty");
        if(json.equals("empty")){
            return myTaskList;
        }
        Type type = new TypeToken<List<Task>>(){}.getType();
        myTaskList = gson.fromJson(json, type);
        return myTaskList;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.myTasks);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<Task> tasks = getMyTaskList();

        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TaskAdapter(tasks, context, activity1);
        rv.setAdapter(mAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.myTasks);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<Task> tasks = getMyTaskList();

        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TaskAdapter(tasks, context, activity1);
        rv.setAdapter(mAdapter);
    }
}