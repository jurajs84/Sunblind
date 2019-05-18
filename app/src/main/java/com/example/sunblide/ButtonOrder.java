package com.example.sunblide;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ButtonOrder {

    private MutableLiveData<String> sunblideInfo;//todo return info to screen

    public ButtonOrder() {
        sunblideInfo.setValue("");
    }

    public void upOn(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.UP_ON);
    }

    public void upOff(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.UP_OFF);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Utility.makeOrder(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                sunblideInfo.setValue(s);
            }
        }

    }
    public MutableLiveData<String> getSunblideInfo() {
        return sunblideInfo;
    }
}
