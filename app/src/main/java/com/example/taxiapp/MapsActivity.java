package com.example.taxiapp;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taxiapp.utils.MapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.taxiapp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button mapButton;
    Marker marker;
    Address address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapButton = findViewById(R.id.map_button);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                LatLng latLng = marker.getPosition();
                Thread addressThread = new Thread(() -> {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    MapUtil mapUtil = new MapUtil();
                    mapUtil.setGeocoder(geocoder);
                    Address result = mapUtil.getAddress(latLng);
                    setAddress(result);
                });
                addressThread.start();
                try {
                    addressThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!address.getLocality().equals("Sankt-Peterburg")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                    alertDialog.setTitle("Wrong location");
                    alertDialog.setMessage(getResources().getString(R.string.warning_message));
                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }
                else {
                    resultIntent.putExtra("longtitude", latLng.longitude);
                    resultIntent.putExtra("latitude", latLng.latitude);
                    resultIntent.putExtra("address", address.getAddressLine(0));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng saintP = new LatLng(59.9311, 30.3609);
        marker = mMap.addMarker(new MarkerOptions().position(saintP).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saintP, 12));



        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                marker = googleMap.addMarker(markerOptions);
            }
        });

    }
}