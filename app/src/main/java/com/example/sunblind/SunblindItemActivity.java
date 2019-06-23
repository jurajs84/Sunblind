package com.example.sunblind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class SunblindItemActivity extends AppCompatActivity implements View.OnTouchListener {

    private String name;
    private String ipAddress;
    private int id;
    private int runningTime;
    private Intent intentData;

    private SunblindItemViewModel sunblindItemViewModel;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sunblind_item_activity);

        Button upBtn = findViewById(R.id.up_button);
        Button downBtn = findViewById(R.id.down_button);
        Button fullUpBtn = findViewById(R.id.full_up_button);
        Button fullDownBtn = findViewById(R.id.full_down_button);
        Button stopBtn = findViewById(R.id.stop_button);
        TextView statusTextView = findViewById(R.id.status_textView);

        upBtn.setOnTouchListener(this);
        downBtn.setOnTouchListener(this);
        fullUpBtn.setOnTouchListener(this);
        fullDownBtn.setOnTouchListener(this);
        stopBtn.setOnTouchListener(this);
        /**
         * subscribe to ViewModel and observe status info from ESP32
         */
        sunblindItemViewModel = ViewModelProviders.of(this).get(SunblindItemViewModel.class);
        sunblindItemViewModel.getStatusInfo().observe(this, statusInfo ->
                statusTextView.setText(statusInfo));

        handler = new Handler();//handler for delaying sunblinds orders
        /**
         * gets intent and sets local variables and sets activity title in ActionBar
         */
       intentData = getIntent();
        if (intentData != null) {
            setData();
        }
    }

    private void setData() {
        name = intentData.getStringExtra(Utility.EXTRA_NAME);
        ipAddress = intentData.getStringExtra(Utility.EXTRA_IP_ADDRESS);
        id = intentData.getIntExtra(Utility.EXTRA_ID, -1);
        runningTime = intentData.getIntExtra(Utility.EXTRA_TIME, 0);
        setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    /**
     * manages edit button and puts extra info to AddEditActivity with EDIT requestCode
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_sunblind:
                Intent sunblindItemData = new Intent(this, AddEditActivity.class);
                sunblindItemData.putExtra(Utility.EXTRA_NAME, name);
                sunblindItemData.putExtra(Utility.EXTRA_IP_ADDRESS, ipAddress);
                sunblindItemData.putExtra(Utility.EXTRA_ID, id);
                sunblindItemData.putExtra(Utility.EXTRA_TIME, runningTime);
                startActivityForResult(sunblindItemData, Utility.EDIT_SUNBLIND_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * waits for result from AdEditActivity and when edit is OK, forwards data to MainActivity
         * and closes this activity
         */
        if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
        /**
         * waits for deleting sunblind is chosen and forwards data to MainActivity with DELETE
         * requestCode for deleting sunblind in MainActivity and closes this activity
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == Utility.DELETE_SUNBLIND) {
            setResult(Utility.DELETE_SUNBLIND, data);
            finish();
        }
        /**
         * when result is cancel from AddEditActivity, sets local variables to proper data
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == RESULT_CANCELED) {
            setData();
        }
    }

    /**
     * manages buttons of each sunblind
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case (R.id.up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setElevation(v.getElevation() + 8);
                    handler.removeCallbacksAndMessages(null);
                    sunblindItemViewModel.downOff(ipAddress);
                    sunblindItemViewModel.upOn(ipAddress);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setElevation(v.getElevation() - 8);
                    sunblindItemViewModel.upOff(ipAddress);
                }
                break;
            case (R.id.down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    sunblindItemViewModel.upOff(ipAddress);
                    sunblindItemViewModel.downOn(ipAddress);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sunblindItemViewModel.downOff(ipAddress);
                }
                break;
            case (R.id.full_up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    sunblindItemViewModel.downOff(ipAddress);
                    sunblindItemViewModel.upOn(ipAddress);
                    handler.postDelayed(() ->
                            sunblindItemViewModel.upOff(ipAddress), runningTime * 1000);
                }
                break;
            case (R.id.full_down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    sunblindItemViewModel.upOff(ipAddress);
                    sunblindItemViewModel.downOn(ipAddress);
                    handler.postDelayed(() ->
                            sunblindItemViewModel.downOff(ipAddress), runningTime * 1000);
                }
                break;
            case (R.id.stop_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    sunblindItemViewModel.upOff(ipAddress);
                    sunblindItemViewModel.downOff(ipAddress);
                }
        }
        return true;
    }
}
