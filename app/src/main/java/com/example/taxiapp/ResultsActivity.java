package com.example.taxiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.taxiapp.model.TaxiDriver;

import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class ResultsActivity extends AppCompatActivity {

    enum SHOW_OPTIONS {MIN, MAX, ALL}

    private TextView departure;
    private TextView arrival;
    private TableLayout table;
    private Button allButton;
    private Button maxButton;
    private Button minButton;
    private ArrayList<TaxiDriver> drivers;
    private SHOW_OPTIONS shown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);
        drivers = new ArrayList<>();
        departure = findViewById(R.id.from);
        arrival = findViewById(R.id.to);
        table = findViewById(R.id.result_table);

        String dest = getIntent().getStringExtra("departure");
        String arr = getIntent().getStringExtra("arrival");
        departure.setText(String.format(getResources().getString(R.string.from_adress), dest));
        arrival.setText(String.format(getResources().getString(R.string.to_adress), arr));
        org.json.simple.JSONArray jsonArray = (JSONArray) JSONValue.parse(getIntent().getStringExtra("API Data"));
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject element = (JSONObject) jsonArray.get(i);
            String name = (String) element.get("name");
            String price = String.valueOf(element.get("price"));
            String rideClass = (String) element.get("rideClass");
            TaxiDriver driver = new TaxiDriver(name, Long.valueOf(price), rideClass);
            drivers.add(driver);
            addDriverToTable(driver);

        }
        shown = SHOW_OPTIONS.ALL;

        allButton = findViewById(R.id.all_button);
        minButton = findViewById(R.id.min_button);
        maxButton = findViewById(R.id.max_button);

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shown == SHOW_OPTIONS.ALL)
                    return;
                table.removeAllViews();
                for (TaxiDriver driver : drivers) {
                    addDriverToTable(driver);
                }
                shown = SHOW_OPTIONS.ALL;
            }
        });

        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shown == SHOW_OPTIONS.MIN)
                    return;
                table.removeAllViews();
                TaxiDriver driver = Collections.min(drivers);
                addDriverToTable(driver);
                shown = SHOW_OPTIONS.MIN;
            }
        });

        maxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shown == SHOW_OPTIONS.MAX)
                    return;
                table.removeAllViews();
                TaxiDriver driver = Collections.max(drivers);
                addDriverToTable(driver);
                shown = SHOW_OPTIONS.MAX;
            }
        });
    }

    public void addDriverToTable(TaxiDriver driver) {
        TableRow row = (TableRow) LayoutInflater.from(ResultsActivity.this).inflate(R.layout.driver_row, null);
        ((TextView) row.findViewById(R.id.name)).setText(driver.getName());
        ((TextView) row.findViewById(R.id.summ)).setText(String.valueOf(driver.getPrice()));
        ((TextView) row.findViewById(R.id.rideClass)).setText(driver.getRideClass());
        table.addView(row);
    }

    public ArrayList<TaxiDriver> getDrivers() {
        return drivers;
    }
}