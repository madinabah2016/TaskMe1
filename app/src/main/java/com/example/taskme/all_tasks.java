package com.example.taskme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class all_tasks extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    public all_tasks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.allTasks);
        rv.setHasFixedSize(true);
        //setHasOptionsMenu(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<Task> tasks = new ArrayList<>();
        //just example
        tasks.add(new Task("Bobby", "Eat cheese", 40519));
        tasks.add(new Task("Madiee", "Eat corn", 12345));

        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TaskAdapter(tasks);
        rv.setAdapter(mAdapter);
        return root;
    }

    //if statement for myTasks
    //return listInfo.length for count in nav pane
}
class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, deadline, assignee;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.taskName);
            deadline = (TextView) view.findViewById(R.id.deadline);
            assignee = (TextView) view.findViewById(R.id.assignee);
        }
    }


    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
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
        String dateStr = Integer.toString(task.getDate());
        holder.deadline.setText(dateStr);
        holder.assignee.setText(task.getAssigned());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}


