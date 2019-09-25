package com.example.sunblind;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.Charset;

public final class Utility {

    public static final int ADD_SUNBLIND_REQUEST = 1;
    public static final int EDIT_SUNBLIND_REQUEST = 2;
    public static final int ITEM_SUNBLIND_REQUEST = 3;
    public static final int DELETE_SUNBLIND = 4;

    public static final String EXTRA_NAME = "com.example.sunblind/EXTRA_NAME";
    public static final String EXTRA_IP_ADDRESS = "com.example.sunblind/EXTRA_IP_ADDRESS";
    public static final String EXTRA_ID = "com.example.sunblind/EXTRA_ID";
    public static final String EXTRA_TIME = "com.example.sunblind/EXTRA_TIME";
    public static final String EXTRA_IP_ARRAY = "com.example.sunblind/EXTRA_IP_ARRAY";
    public static final String EXTRA_MAX_RUNNING_TIME = "com.example.sunblind/EXTRA_MAX_RUNNING_TIME";

    public static final int DEFAULT_TIME = 3000;
    public static final String IP_ADDRESS_KITCHEN_A = "192.168.43.82";
    public static final String UP_ON = "up_on";
    public static final String UP_OFF = "up_off";
    public static final String DOWN_ON = "down_on";
    public static final String DOWN_OFF = "down_off";
    public static final String STATUS = "status";

    public static String makeOrder(String order) {
        URL url = createUrl(order);
        String response = "offline";
        try {
            response = httpConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String httpConnection(URL url) throws IOException {
        String jsonData = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream =null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(500);//short time
            urlConnection.setConnectTimeout(500);//short time
            urlConnection.setRequestMethod("PUT");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonData = readFromStream(inputStream);
            } else {
                Log.e("LOG", "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("LOG","Error getting json data " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        Log.e("LOG", "jsonData: " + jsonData);
        return jsonData;
    }

    private static URL createUrl(String order) {
        URL url = null;
        try {
            url = new URL(order);
        } catch (MalformedURLException e) {
            Log.e("LOG","Error creating URL");
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
}
