package com.example.sunblind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class ControlActivity extends AppCompatActivity implements View.OnTouchListener{

    private ControlActivityViewModel controlActivityViewModel;
    private SunblindRecyclerViewAdapter adapter;
    private Sunblind sunblindToDelete;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_control);

        Button btnUp = findViewById(R.id.btn_up);
        Button btnDown = findViewById(R.id.btn_down);
        Button btnFullUp = findViewById(R.id.btn_full_up);
        Button btnFullDown = findViewById(R.id.btn_full_down);
        Button btnStop = findViewById(R.id.btn_stop);

        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnFullUp.setOnTouchListener(this);
        btnFullDown.setOnTouchListener(this);
        btnStop.setOnTouchListener(this);

        /*
          setting RecyclerView with GridLayoutManager with two columns
          and subscribe to observing sunblindList from SunblindViewModel
         */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new SunblindRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        controlActivityViewModel = new ViewModelProvider(this).get(ControlActivityViewModel.class);
        controlActivityViewModel.getAllSunblindList().observe(this, allSunblindList -> {
            adapter.setSunblindList(allSunblindList);//sending allList to adapter
        });
        /*
         * subscribe to ViewModel and observe status info from ESP32
         */
        controlActivityViewModel.getStatusInfo().observe(this, status -> adapter.setStatusIcons(status));

        handler = new Handler();//handler for delaying sunblinds orders

        /*
          continuing creating clickable Sunblinds
          4. implementing setOnItemClickListener from our adapter
          creating Intent to open SunblindItemActivity and sending extras with EDIT requestCode
         */
        adapter.setOnItemLongClickListener(sunblind -> {
            //because we are in anonymous class we cannot call only this
            Intent intent = new Intent(ControlActivity.this, AddEditActivity.class);
            intent.putExtra(Utility.EXTRA_NAME, sunblind.getName());
            intent.putExtra(Utility.EXTRA_IP_ADDRESS, sunblind.getAddress());
            intent.putExtra(Utility.EXTRA_TIME, sunblind.getRunningTime());
            //add sunblind id to distinguish in AddEdit activity if it is editing or adding
            intent.putExtra(Utility.EXTRA_ID, sunblind.getId());
            startActivityForResult(intent, Utility.EDIT_SUNBLIND_REQUEST);
            /*
              saving sunblind to sunblindToDelete in case of selecting delete sunblind in
              AddEditActivity
            */
            sunblindToDelete = sunblind;
        });
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        isSunblindOnline();
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int runningTime = adapter.getSelectedMaxRunningTime();
        if (runningTime == 0) {//if runningTime == 0 -> no sunblide selected -> ends onTouch method
            return false;
        }

        String[] allIp = adapter.getSelectedIp();

        switch (v.getId()) {
            case (R.id.btn_up):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlActivityViewModel.allSelectedDownOff(allIp);
                    controlActivityViewModel.allSelectedUpOn(allIp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlActivityViewModel.allSelectedUpOff(allIp);
                }
                break;
            case (R.id.btn_down):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlActivityViewModel.allSelectedUpOff(allIp);
                    controlActivityViewModel.allSelectedDownOn(allIp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlActivityViewModel.allSelectedDownOff(allIp);
                }
                break;
            case (R.id.btn_full_up):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlActivityViewModel.allSelectedDownOff(allIp);
                    controlActivityViewModel.allSelectedUpOn(allIp);
                    handler.postDelayed(() ->
                            controlActivityViewModel.allSelectedUpOff(allIp), runningTime * 1000);
                }
                break;
            case (R.id.btn_full_down):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlActivityViewModel.allSelectedUpOff(allIp);
                    controlActivityViewModel.allSelectedDownOn(allIp);
                    handler.postDelayed(() ->
                            controlActivityViewModel.allSelectedDownOff(allIp), runningTime * 1000);
                }
                break;
            case (R.id.btn_stop):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeCallbacksAndMessages(null);
                    controlActivityViewModel.allSelectedUpOff(allIp);
                    controlActivityViewModel.allSelectedDownOff(allIp);
                }
        }
        return true;
    }

    private void isSunblindOnline() {
        String[] ipList = adapter.getAllIp();
        if (ipList.length > 0) {
            for (String ip : ipList) {
                controlActivityViewModel.getStatus(ip);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSunblindOnline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
          waiting for result from adding sunblind
         */
        if (requestCode == Utility.ADD_SUNBLIND_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String name = data.getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);
            int runningTime = data.getIntExtra(Utility.EXTRA_TIME, 0);

            Sunblind sunblind = new Sunblind(name, ipAddress, runningTime);
            controlActivityViewModel.insert(sunblind);
            Toast.makeText(this, "New sunblind saved", Toast.LENGTH_SHORT).show();
        }
        /*
          waiting for result from edit sunblind
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == RESULT_OK) {
            String name = Objects.requireNonNull(data).getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);
            int runningTime = data.getIntExtra(Utility.EXTRA_TIME, 0);
            int id = data.getIntExtra(Utility.EXTRA_ID, -1);

            Sunblind sunblind = new Sunblind(name, ipAddress, runningTime);
            sunblind.setId(id);
            controlActivityViewModel.update(sunblind);
            Toast.makeText(this, "Sunblind updated", Toast.LENGTH_SHORT).show();
            // if sunblind is not updated, id gets default value -1
            if (id == -1) {
                Toast.makeText(this, "Sunblind cannot be updated", Toast.LENGTH_SHORT).show();
            }
        }
        /*
          waiting for result when sunblind is deleted
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == Utility.DELETE_SUNBLIND){
            controlActivityViewModel.delete(sunblindToDelete);
            Toast.makeText(this, "Sunblind deleted", Toast.LENGTH_SHORT).show();
        }
        //TODO toast message for not saved
//        else if (requestCode != Utility.ITEM_SUNBLIND_REQUEST || ){
//            Toast.makeText(this, "Sunblind not saved", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    /**
     * saving new sunblind and putting ADD requestCode
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_sunblind:
                Intent editIntent = new Intent(this, AddEditActivity.class);
                startActivityForResult(editIntent, Utility.ADD_SUNBLIND_REQUEST);
                return true;
            case R.id.refresh:
                isSunblindOnline();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
