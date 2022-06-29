package com.example.taxiapp.utils;

import android.location.Address;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.taxiapp.MapsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class JsonUtil {

    private static String urlString = "";
    private static String json;

    public String getJSONObjectFromURL(String urlString) throws IOException, JSONException, ExecutionException, InterruptedException {
        JsonUtil.urlString = urlString;

        Thread jsonThread = new Thread(() -> {
            HttpURLConnection urlConnection = null;
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            try {
                urlConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }


            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();

            String line = null;
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line + "\n");
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonString = sb.toString();
            json = jsonString;
        });
        jsonThread.start();
        try {
            jsonThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JsonUtil.json;
    }
}
