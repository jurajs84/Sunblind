package com.example.sunblind;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

class ButtonOrder {

    private final MutableLiveData<String> sunblindInfo;

    /**
     * constructor initializes LiveData sunblindInfo
     */
    public ButtonOrder() {
        sunblindInfo = new MutableLiveData<>();
    }

    /**
     * next methods create url and async task to send order via Utility class
     * to particular ESP32 define by ip address
     * @param ip ip address of particular ESP32
     */
    public void upOn(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.UP_ON);
    }

    public void upOff(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.UP_OFF);
    }

    public void downOn(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.DOWN_ON);
    }

    public void downOff(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.DOWN_OFF);
    }

    public void getStatusInfo(String ip) {
        new MyAsyncTask().execute("http://" + ip + "/" + Utility.STATUS);
    }

    /**
     * MyAsyncTask returns String info from ESP32. It returns ip when ESP32 is online and empty
     * String when offline. This String info sets LiveData sunblindInfo
     */
    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Utility.makeOrder(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                sunblindInfo.setValue(s);
            }
        }
    }

    public MutableLiveData<String> getSunblindInfo() {
        return sunblindInfo;
    }
}
