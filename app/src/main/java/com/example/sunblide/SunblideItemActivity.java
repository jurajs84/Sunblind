package com.example.sunblide;

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

public class SunblideItemActivity extends AppCompatActivity implements View.OnTouchListener {

    private String name;
    private String ipAddress;
    private int id;
    private Intent intentData;

    private SunblideItemViewModel sunblideItemViewModel;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sunblide_item_activity);

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

        sunblideItemViewModel = ViewModelProviders.of(this).get(SunblideItemViewModel.class);
        sunblideItemViewModel.getStatusInfo().observe(this, statusInfo ->
                statusTextView.setText(statusInfo));

        handler = new Handler();


       intentData = getIntent();
        if (intentData != null) {
            setData();
        }
    }

    private void setData() {
        name = intentData.getStringExtra(Utility.EXTRA_NAME);
        ipAddress = intentData.getStringExtra(Utility.EXTRA_IP_ADDRESS);
        id = intentData.getIntExtra(Utility.EXTRA_ID, -1);
        setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_sunblide:
                Intent sunblideItemData = new Intent(this, AddEditActivity.class);
                sunblideItemData.putExtra(Utility.EXTRA_NAME, name);
                sunblideItemData.putExtra(Utility.EXTRA_IP_ADDRESS, ipAddress);
                sunblideItemData.putExtra(Utility.EXTRA_ID, id);
                //sunblideItemData.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivityForResult(sunblideItemData, Utility.EDIT_SUNBLIDE_REQUEST);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.EDIT_SUNBLIDE_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
        else if (requestCode == Utility.EDIT_SUNBLIDE_REQUEST && resultCode == Utility.DELETE_SUNBLIDE) {
            setResult(Utility.DELETE_SUNBLIDE, data);
            finish();
        }
        else if (requestCode == Utility.EDIT_SUNBLIDE_REQUEST && resultCode == RESULT_CANCELED) {
            setData();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case (R.id.up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sunblideItemViewModel.upOn(ipAddress);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sunblideItemViewModel.upOff(ipAddress);
                }
                break;
            case (R.id.down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //todo
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //todo
                }
                break;
            case (R.id.full_up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sunblideItemViewModel.upOn(ipAddress);
                    handler.postDelayed(() ->
                            sunblideItemViewModel.upOff(ipAddress), Utility.DEFAULT_TIME);
                }
                break;
            case (R.id.full_down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //todo
                }
                break;
            case (R.id.stop_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    sunblideItemViewModel.upOff(ipAddress);
                }
        }
        return true;
    }
}
