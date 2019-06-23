package com.example.sunblind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ControlAllActivity extends AppCompatActivity implements View.OnTouchListener {

    private String[] allIp;
    private int runningTime;

    ControlAllActivityViewModel controlAllActivityViewModel;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_all);
        setTitle("All sunblinds control");

        Button allUpBtn = findViewById(R.id.all_up_button);
        Button allDownBtn = findViewById(R.id.all_down_button);
        Button allFullUpBtn = findViewById(R.id.all_full_up_button);
        Button allFullDownBtn = findViewById(R.id.all_full_down_button);
        Button allStopBtn = findViewById(R.id.all_stop_button);

        allUpBtn.setOnTouchListener(this);
        allDownBtn.setOnTouchListener(this);
        allFullUpBtn.setOnTouchListener(this);
        allFullDownBtn.setOnTouchListener(this);
        allStopBtn.setOnTouchListener(this);

        handler = new Handler();//handler for delaying sunblinds orders
        controlAllActivityViewModel = new ControlAllActivityViewModel();

        Intent mainActivityIntent = getIntent();
        if (mainActivityIntent.hasExtra(Utility.EXTRA_IP_ARRAY)) {
            allIp = mainActivityIntent.getStringArrayExtra(Utility.EXTRA_IP_ARRAY);
            runningTime = mainActivityIntent.getIntExtra(Utility.EXTRA_MAX_RUNNING_TIME, 0);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case (R.id.all_up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlAllActivityViewModel.allDownOff(allIp);
                    controlAllActivityViewModel.allUpOn(allIp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlAllActivityViewModel.allUpOff(allIp);
                }
                break;
            case (R.id.all_down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlAllActivityViewModel.allUpOff(allIp);
                    controlAllActivityViewModel.allDownOn(allIp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlAllActivityViewModel.allDownOff(allIp);
                }
                break;
            case (R.id.all_full_up_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlAllActivityViewModel.allDownOff(allIp);
                    controlAllActivityViewModel.allUpOn(allIp);
                    handler.postDelayed(() ->
                            controlAllActivityViewModel.allUpOff(allIp), runningTime * 1000);
                }
                break;
            case (R.id.all_full_down_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlAllActivityViewModel.allUpOff(allIp);
                    controlAllActivityViewModel.allDownOn(allIp);
                    handler.postDelayed(() ->
                            controlAllActivityViewModel.allDownOff(allIp), runningTime * 1000);
                }
                break;
            case (R.id.all_stop_button):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlAllActivityViewModel.allUpOff(allIp);
                    controlAllActivityViewModel.allDownOff(allIp);
                }
        }
        return true;
    }
}
