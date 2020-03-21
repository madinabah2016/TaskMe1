package com.example.taskme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment addTaskFrag;
    private Fragment allTaskFrag;
    private Fragment myTaskFrag;
    private RecyclerView recView;
    private RecyclerView.Adapter recAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("TaskObjects1", Activity.MODE_PRIVATE);

        ArrayList<Task> taskList = getTaskList();
        int length = taskList.size();



        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        addTaskFrag = new addTask();
        allTaskFrag = new all_tasks();
        myTaskFrag = new myTasks();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, allTaskFrag).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView taskToDoNum = header.findViewById(R.id.taskToDoNum);
        String numString = length + "Tasks To Do";
        taskToDoNum.setText(numString);

        navigationView.setNavigationItemSelectedListener(this);
    }

    public ArrayList<Task> getTaskList() {

        ArrayList<Task> taskList = new ArrayList<Task>();
        Gson gson = new Gson();
        String json = prefs.getString("TaskObjects1", "empty");
        if (json.equals("empty")) {
            return taskList;
        }
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        taskList = gson.fromJson(json, type);

        return taskList;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tasks) {
            getSupportActionBar().setTitle("All Tasks");
            transaction.replace(R.id.fragment_container, allTaskFrag);
            transaction.addToBackStack(null);
            transaction.commit();
            // Handle the camera action
        } else if (id == R.id.myTasks) {
            getSupportActionBar().setTitle("My Tasks");
            transaction.replace(R.id.fragment_container, myTaskFrag);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.addTasks) {
            getSupportActionBar().setTitle("Add Task");
            transaction.replace(R.id.fragment_container, addTaskFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent setting = new Intent(this, settings.class);
            startActivity(setting);
        }
        return super.onOptionsItemSelected(item);
    }
}
