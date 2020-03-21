package com.example.taskme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class settings extends AppCompatActivity {
    String days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Settings");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> arrayItems = new ArrayList<>();
        arrayItems.add("1 day");
        arrayItems.add("3 days");
        arrayItems.add("7 days");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_settings, arrayItems);
        arrayAdapter.setDropDownViewResource(R.layout.activity_settings);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                days = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(parent.getContext(), "Pick a duration", ).show();
            }
        });
    }
}
