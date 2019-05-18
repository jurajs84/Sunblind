package com.example.sunblide;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ButtonOrder {

    private MutableLiveData<String> sunblideInfo;

    public ButtonOrder() {
        sunblideInfo = new MutableLiveData<>();
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
            String result = Utility.makeOrder(strings[0]);
            if (result != null){
                return result;
            }
            return null;
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
