package com.example.sunblind;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    SunblindViewModel sunblindViewModel;
    SunblindRecyclerViewAdapter adapter;
    Sunblind sunblindToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * setting RecyclerView with GridLayoutManager with two columns
         * and subscribe to observing sunblindList from SunblindViewModel
         */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new SunblindRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        sunblindViewModel = ViewModelProviders.of(this).get(SunblindViewModel.class);
        sunblindViewModel.getAllSunblindList().observe(this, allSunblindList -> {
            adapter.setSunblindList(allSunblindList);//sending allList to adapter
        });

        /**
         * continuing creating clickable Sunblinds
         * 4. implementing setOnItemClickListener from our adapter
         * creating Intent to open SunblindItemActivity and sending extras with EDIT requestCode
         */
        adapter.setOnItemClickListener(sunblind -> {
            //because we are in anonymous class we cannot call only this
            Intent intent = new Intent(MainActivity.this, SunblindItemActivity.class);
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
            case R.id.control_all_sunblinds:
                if (adapter.getItemCount() > 0) {
                    Intent allSunblindsIntent =
                            new Intent(this, ControlAllActivity.class);
                    allSunblindsIntent.putExtra(Utility.EXTRA_IP_ARRAY, adapter.getAllIp());
                    allSunblindsIntent.putExtra(Utility.EXTRA_MAX_RUNNING_TIME, adapter.getMaxRunningTime());
                    startActivity(allSunblindsIntent);
                }
                else {
                    Toast.makeText(this, "First add sunblind", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * waiting for result from adding sunblind
         */
        if (requestCode == Utility.ADD_SUNBLIND_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);
            int runningTime = data.getIntExtra(Utility.EXTRA_TIME, 0);

            Sunblind sunblind = new Sunblind(name, ipAddress, runningTime);
            sunblindViewModel.insert(sunblind);
            Toast.makeText(this, "New sunblind saved", Toast.LENGTH_SHORT).show();
        }
        /**
         * waiting for result from edit sunblind
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);
            int runningTime = data.getIntExtra(Utility.EXTRA_TIME, 0);
            int id = data.getIntExtra(Utility.EXTRA_ID, -1);

            Sunblind sunblind = new Sunblind(name, ipAddress, runningTime);
            sunblind.setId(id);
            sunblindViewModel.update(sunblind);
            Toast.makeText(this, "Sunblind updated", Toast.LENGTH_SHORT).show();
            // if sunblind is not updated, id gets default value -1
            if (id == -1) {
                Toast.makeText(this, "Sunblind cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /**
         * waiting for result when sunblind is deleted
         */
        else if (requestCode == Utility.EDIT_SUNBLIND_REQUEST && resultCode == Utility.DELETE_SUNBLIND){
            sunblindViewModel.delete(sunblindToDelete);
            Toast.makeText(this, "Sunblind deleted", Toast.LENGTH_SHORT).show();
        }
        //TODO toast message for not saved
//        else if (requestCode != Utility.ITEM_SUNBLIND_REQUEST || ){
//            Toast.makeText(this, "Sunblind not saved", Toast.LENGTH_SHORT).show();
//        }
    }
}
