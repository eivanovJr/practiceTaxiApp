package com.example.taxiapp.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.TimeZone;

public class URLUtilsTest {

    private static final LatLng departure = new LatLng(50.0, 39.0);
    private static final LatLng arrival = new LatLng(34.32, 45.45);

    private static final String expectedYandexString = "https://taxi-routeinfo.taxi.yandex.net/taxi_info?clid=" +
            "testClid&apikey=testkey&rll=39.0,50.0~45.45,34.32";
    private static final String expectedCustomString = "http://51.250.101.124:8080/api/ride/price?startlat=50.0&startlon=39.0" +
            "&endlat=34.32&endlon=45.45";

    @Test
    public void createYandexURL() throws MalformedURLException {
        String http = "https://taxi-routeinfo.taxi.yandex.net/taxi_info?";
        try {
            assert URLUtils.createYandexURL(departure, arrival, http, "testkey", "testClid").toString().equals(expectedYandexString);
        }
        catch (Exception e) {
            throw new Error("caught exception");
        }
    }

    @Test
    public void createCustomServiceURLWithoutTime() {
        String http = "http://51.250.101.124:8080/api/ride/price?";
        Integer waitingTime = 0;
        assert URLUtils.createCustomServiceURL(departure, arrival, waitingTime, http).equals(expectedCustomString);
    }

    @Test
    public void createCustomServiceURLWithTime() {
        String http = "http://51.250.101.124:8080/api/ride/price?";
        Integer waitingTime = 10;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
        calendar.add(Calendar.MINUTE, waitingTime);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String time = "&time=" + hours + "_" + minutes;
        assert URLUtils.createCustomServiceURL(departure, arrival, waitingTime, http).equals(expectedCustomString + time);
    }
}