package com.example.sunblide;

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

    public static final int ADD_SUNBLIDE_REQUEST = 1;
    public static final int EDIT_SUNBLIDE_REQUEST = 2;
    public static final int ITEM_SUNBLIDE_REQUEST = 3;
    public static final int DELETE_SUNBLIDE = 4;

    public static final String EXTRA_NAME = "com.example.sunblide/EXTRA_NAME";
    public static final String EXTRA_IP_ADDRESS = "com.example.sunblide/EXTRA_IP_ADDRESS";
    public static final String EXTRA_ID = "com.example.sunblide/EXTRA_ID";

    public static final int DEFAULT_TIME = 3;
    public static final String IP_ADDRESS_KITCHEN_A = "192.168.43.82";
    public static final String UP_ON = "up_on";
    public static final String UP_OFF = "up_off";

    public static String makeOrder(String order) {
        URL url = createUrl(order);
        String response = "";
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
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonData = readFromStream(inputStream);
            } else {
                Log.e("LOG", "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("LOG1","Error getting json data " + urlConnection.getResponseCode());
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
