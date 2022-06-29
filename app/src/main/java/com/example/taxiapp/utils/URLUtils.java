package com.example.taxiapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.taxiapp.MainActivity;
import com.example.taxiapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

public class URLUtils {

    public static URL createYandexURL(LatLng departureCoordinates, LatLng arrivalCoordinates, String httpMain, String apikey, String clidValue) throws MalformedURLException {
        String clid = "clid=" + clidValue;
        String apiKey = "&apikey=" + apikey;
        String coordinates = "&rll=" + departureCoordinates.longitude + "," + departureCoordinates.latitude + "~"
                + arrivalCoordinates.longitude + "," + arrivalCoordinates.latitude;
        return new URL(httpMain + clid + apiKey + coordinates);
    }

    public static String createCustomServiceURL(LatLng departureCoordinates, LatLng arrivalCoordinates, Integer waitingTime, String http_main) {
        String startlat = "startlat=" + departureCoordinates.latitude;
        String startlong = "&startlon=" + departureCoordinates.longitude;
        String endlat = "&endlat=" + arrivalCoordinates.latitude;
        String endlong = "&endlon=" + arrivalCoordinates.longitude;
        String time = "";
        if (waitingTime != 0) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
            calendar.add(Calendar.MINUTE, waitingTime);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            time = "&time=" + hours + "_" + minutes;
        }
        return new String(http_main + startlat + startlong + endlat + endlong + time);
    }
}
