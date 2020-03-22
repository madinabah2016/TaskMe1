package com.example.taskme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class settings extends AppCompatActivity {
    String days;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mPrefs = getSharedPreferences("Settings Pref", Activity.MODE_PRIVATE);
        editor = mPrefs.edit();

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = (EditText) findViewById(R.id.username);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString("username", name.getText().toString() + "!");
            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("username", name.getText().toString() + "!");
            }
        });
        /*name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    editor.putString("username", name.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    return true;}
                return false;
            }
        });
        */
        editor.commit();


        Switch toggle = (Switch) findViewById(R.id.switch1);
        final Boolean toggleState = toggle.isChecked();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> arrayItems = new ArrayList<>();
        arrayItems.add("1 day");
        arrayItems.add("3 days");
        arrayItems.add("7 days");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayItems);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                days = parent.getItemAtPosition(position).toString();
                if (toggleState) {
                    editor.putString("notification_days", days);
                } else {
                    editor.putString("notification_days", "none");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Pick a duration", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        editor.putInt("index", spin.getSelectedItemPosition()).apply();
        Switch toggle = (Switch) findViewById(R.id.switch1);
        editor.putBoolean("bool", toggle.isChecked()).apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setSelection(mPrefs.getInt("index", 0));
        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(mPrefs.getBoolean("bool", false));
    }
}
