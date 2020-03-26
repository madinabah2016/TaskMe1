package com.example.taskme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */

public class all_tasks extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    private SharedPreferences mPrefs;
    private SharedPreferences myPrefs;
    public Context context;
    private Activity activity1;

    public all_tasks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All Tasks");
        mPrefs = getContext().getSharedPreferences("TaskObjects1", Activity.MODE_PRIVATE);
        myPrefs = getContext().getSharedPreferences("TaskObjects2", Activity.MODE_PRIVATE);
        context = getContext();
        activity1 = getActivity();


        RecyclerView rv = (RecyclerView) root.findViewById(R.id.allTasks);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<Task> tasks = getTaskList();


        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TaskAdapter(tasks, context, activity1);
        rv.setAdapter(mAdapter);

        SharedPreferences specialPref = getContext().getSharedPreferences("Special", Activity.MODE_PRIVATE);
        SharedPreferences.Editor specialPrefEditor = specialPref.edit();
        specialPrefEditor.putBoolean("flag", false);
        specialPrefEditor.commit();

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Task");
                Fragment addTaskFrag = new addTask();
                transaction.replace(R.id.fragment_container, addTaskFrag);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        TextView taskToDoNum = header.findViewById(R.id.taskToDoNum);
        ArrayList<Task> mytaskList = getMyTaskList();
        int length = mytaskList.size();
        String numString = length + " Tasks To Do";
        taskToDoNum.setText(numString);
        return root;
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

}

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;
    private Context context;
    private Activity activity1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, deadline, assignee;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.taskName);
            deadline = (TextView) view.findViewById(R.id.deadline);
            assignee = (TextView) view.findViewById(R.id.assigneeDisplay);
            view.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity1, view_tasks.class);
            intent.putExtra("Task Name", taskList.get(getAdapterPosition()).getTask());
            intent.putExtra("Assignee Name", taskList.get(getAdapterPosition()).getAssigned());
            intent.putExtra("Deadline", taskList.get(getAdapterPosition()).getDate());
            intent.putExtra("Position", getAdapterPosition());
            context.startActivity(intent);


        }


    }

    public TaskAdapter(List<Task> taskList, Context context, Activity activity1) {
        this.taskList = taskList;
        this.context = context;
        this.activity1 = activity1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.name.setText(task.getTask());
        holder.deadline.setText(task.getDate());
        holder.assignee.setText(task.getAssigned());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}


