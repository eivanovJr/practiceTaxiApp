package com.example.taxiapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxiapp.databinding.ActivityMainBinding;
import com.example.taxiapp.utils.JsonUtil;
import com.example.taxiapp.utils.URLUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView departure;
    private TextView arrival;
    private EditText time;
    private Button button;
    private ImageButton mapButtonDeparture;
    private ImageButton mapButtonArrival;
    ActivityResultLauncher<Intent> mapArrivalActivityLauncher;
    ActivityResultLauncher<Intent> mapDepartureActivityLauncher;
    LatLng departureCoordinates;
    LatLng arrivalCoordinates;
    private String json;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        departure = findViewById(R.id.departure);
        arrival = findViewById(R.id.arrival);
        time = findViewById(R.id.time);
        button = findViewById(R.id.main_button);
        mapButtonDeparture = findViewById(R.id.maps_button1);
        mapButtonArrival = findViewById(R.id.maps_button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departure.getText().toString().matches("") || arrival.getText().toString().matches(""))
                {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.direction_warning), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Integer mins = 0;
                    if (!time.getText().toString().equals(""))
                    {
                        mins = Integer.parseInt(time.getText().toString());
                        if ((mins <= 0 || mins > 15)) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.time_bounds), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sendData(mins);
                        }
                    }
                    else {
                        sendData(mins);
                    }
                }
                catch (NumberFormatException | PackageManager.NameNotFoundException | IOException | JSONException | ParseException e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.time_not_number), Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mapButtonArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                mapArrivalActivityLauncher.launch(intent);
            }
        });

        mapButtonDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                mapDepartureActivityLauncher.launch(intent);
            }
        });

        mapArrivalActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String arr = data.getStringExtra("address");
                            arrival.setText(arr);
                            double longtitude = data.getDoubleExtra("longtitude", 0.0);
                            double latitude = data.getDoubleExtra("latitude", 0.0);
                            arrivalCoordinates = new LatLng(latitude, longtitude);
                        }
                    }
                }
        );

        mapDepartureActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String str = data.getStringExtra("address");
                            departure.setText(str);
                            double longtitude = data.getDoubleExtra("longtitude", 0.0);
                            double latitude = data.getDoubleExtra("latitude", 0.0);
                            departureCoordinates = new LatLng(latitude, longtitude);
                        }
                    }
                }
        );

    }

    protected String getCustomServiceData(Integer waitingTime) throws IOException, JSONException, ExecutionException, InterruptedException, ParseException {
        String http = getResources().getString(R.string.custom_http);
        String customUrl = URLUtils.createCustomServiceURL(departureCoordinates, arrivalCoordinates, waitingTime, http);
        return new JsonUtil().getJSONObjectFromURL(customUrl);
    }

    protected void sendData(Integer waitingTime) throws PackageManager.NameNotFoundException, IOException, JSONException, ExecutionException, InterruptedException, ParseException {
        ApplicationInfo info = getPackageManager().getApplicationInfo(MainActivity.this.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = info.metaData;
        URL yandex_url = URLUtils.createYandexURL(departureCoordinates, arrivalCoordinates,
                getResources().getString(R.string.https_body).toString(),
                bundle.getString("YANDEX_API_KEY"), bundle.getString("YANDEX_CLID"));
        String jsonArray = "";
        try {
           jsonArray = getCustomServiceData(waitingTime);
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to get JSON from server", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println(yandex_url.toString());

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("departure", departure.getText().toString());
        intent.putExtra("arrival", arrival.getText().toString());
        intent.putExtra("API Data", jsonArray);
        startActivity(intent);
    }
}