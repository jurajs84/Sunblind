package com.example.sunblide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {



    SunblideViewModel sunblideViewModel;
    SunblideRecyclerViewAdapter adapter;
    Sunblide sunblideToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting RecyclerViewAdapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new SunblideRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        sunblideViewModel = ViewModelProviders.of(this).get(SunblideViewModel.class);
        sunblideViewModel.getAllSunblideList().observe(this, allSunblideList -> {
            adapter.setSunblideList(allSunblideList);//sending allList to adapter
        });



        Button upBtn = findViewById(R.id.all_up_btn);
        upBtn.setOnTouchListener(this);

        Button downBtn = findViewById(R.id.all_down_btn);
        downBtn.setOnTouchListener(this);

        Button upFullBtn = findViewById(R.id.all_up_full_btn);
        upFullBtn.setOnTouchListener(this);

        Button downFullBtn = findViewById(R.id.all_down_full_btn);
        downFullBtn.setOnTouchListener(this);

        TextView pingTextView = findViewById(R.id.pingTextView);

        isOnLine();

//        sunblideViewModel = new SunblideViewModel();

        sunblideViewModel.getPingInfo().observe(this, pingInfo ->{
            if (pingInfo.matches("ok")) {
                pingTextView.setText("Kuchyně online");
            }
            else {
                pingTextView.setText("Kuchyně offline");
            }
        });

        //continuing creating clickable Sunblides
        //4. implementing setOnItemClickListener from our adapter
        adapter.setOnItemClickListener(sunblide -> {
            //because we are in anonymous class we cannot call only this
            Intent intent = new Intent(MainActivity.this, SunblideItemActivity.class);
            intent.putExtra(Utility.EXTRA_NAME, sunblide.getName());
            intent.putExtra(Utility.EXTRA_IP_ADDRESS, sunblide.getAddress());
            //add sunblide id to distinguish in AddEdit activity if it is editing or adding
            intent.putExtra(Utility.EXTRA_ID, sunblide.getId());
            startActivityForResult(intent, Utility.EDIT_SUNBLIDE_REQUEST);
            sunblideToDelete = sunblide;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_sunblide:
                Intent intent = new Intent(this, AddEditActivity.class);
                startActivityForResult(intent, Utility.ADD_SUNBLIDE_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utility.ADD_SUNBLIDE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);

            Sunblide sunblide = new Sunblide(name, ipAddress);
            sunblideViewModel.insert(sunblide);
            Toast.makeText(this, "New sunblide saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == Utility.EDIT_SUNBLIDE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(Utility.EXTRA_NAME);
            String ipAddress = data.getStringExtra(Utility.EXTRA_IP_ADDRESS);
            int id = data.getIntExtra(Utility.EXTRA_ID, -1);

            Sunblide sunblide = new Sunblide(name, ipAddress);
            sunblide.setId(id);
            sunblideViewModel.update(sunblide);
            Toast.makeText(this, "Sunblide updated", Toast.LENGTH_SHORT).show();
            if (id == -1) {
                Toast.makeText(this, "Sunblide cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if (requestCode == Utility.EDIT_SUNBLIDE_REQUEST && resultCode == Utility.DELETE_SUNBLIDE){
            sunblideViewModel.delete(sunblideToDelete);
            Toast.makeText(this, "Sunblide deleted", Toast.LENGTH_SHORT).show();
        }
//        else if (requestCode != Utility.ITEM_SUNBLIDE_REQUEST || ){
//            Toast.makeText(this, "Sunblide not saved", Toast.LENGTH_SHORT).show();
//        }
    }

    private void isOnLine() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case (R.id.all_up_btn):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sunblideViewModel.upOn(Utility.IP_ADDRESS_KITCHEN_A);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sunblideViewModel.upOff(Utility.IP_ADDRESS_KITCHEN_A);
            }
            break;
            case (R.id.all_down_btn):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //sunblideViewModel.upOff();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    downOff();
            }
            break;
            case (R.id.all_up_full_btn):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    upOn();
                }
            break;
            case (R.id.all_down_full_btn):
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downOn();
                }
            break;
        }
        return true;
    }

    private void downOff() {
        Toast.makeText(this, "down OFF", Toast.LENGTH_SHORT).show();
    }

    private void downOn() {
        Toast.makeText(this, "down ON", Toast.LENGTH_SHORT).show();
    }

    private void upOff() {
        Toast.makeText(this, "up OFF", Toast.LENGTH_SHORT).show();
    }

    private void upOn() {
        Toast.makeText(this, "up ON", Toast.LENGTH_SHORT).show();
    }
}
