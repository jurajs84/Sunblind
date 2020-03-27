package com.example.sunblind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity implements View.OnTouchListener {

    private EditText nameEditText;
    private EditText ipAddressEditText;
    private FloatingActionButton checkFab;
    private NumberPicker numberPicker;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);

        nameEditText = findViewById(R.id.name_editText);
        ipAddressEditText = findViewById(R.id.ip_editText);
        checkFab = findViewById(R.id.add_fab);
        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(120);
//        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
//
//        });

        /*
          gets intent after creating this activity and sets proper title text
          if intent has EXTRA_ID, it means that we want to edit existing sunblind
         */
        intent = getIntent();
        if (intent.hasExtra(Utility.EXTRA_ID)){
            setTitle("Edit sunblind");
            //settings EditTexts to proper values
            nameEditText.setText(intent.getStringExtra(Utility.EXTRA_NAME));
            ipAddressEditText.setText(intent.getStringExtra(Utility.EXTRA_IP_ADDRESS));
            numberPicker.setValue(intent.getIntExtra(Utility.EXTRA_TIME, 0));
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        }
        else {
            setTitle("Add sunblind");
        }

        checkFab.setOnClickListener(view -> saveSunblind());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (intent.hasExtra(Utility.EXTRA_ID)) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.delete_sunblind, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.delete_sunblind:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Alert dialog prevents unintended deleting
     */
    private void showAlertDialog() {
        new AlertDialog.Builder(this, R.style.AlertDialog)
                .setMessage("Do you really want to delete it?")
                .setPositiveButton("OK", (dialog, which) -> deleteSunblind())
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void deleteSunblind() {
        String name = nameEditText.getText().toString();
        String ipAddress = ipAddressEditText.getText().toString();
        int runningTime = numberPicker.getValue();
        setResult(Utility.DELETE_SUNBLIND, makeNewIntent(name, ipAddress, runningTime));
        finish();
    }

    private void saveSunblind() {
        String name = nameEditText.getText().toString().trim();
        String ipAddress = ipAddressEditText.getText().toString().trim();
        int runningTime = numberPicker.getValue();

        if (name.isEmpty()){
            Toast.makeText(this, "Add name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ipAddress.isEmpty()){
            Toast.makeText(this, "Add IP address", Toast.LENGTH_SHORT).show();
            return;
        }
        //sending data to MainActivity via SunblindItemActivity activity
        setResult(RESULT_OK, makeNewIntent(name, ipAddress, runningTime));
        finish();
    }

    private Intent makeNewIntent(String name, String ipAddress, int time) {
        Intent intent = new Intent(this, ControlActivity.class);
        intent.putExtra(Utility.EXTRA_NAME, name);
        intent.putExtra(Utility.EXTRA_IP_ADDRESS, ipAddress);
        intent.putExtra(Utility.EXTRA_TIME, time);
        int id = getIntent().getIntExtra(Utility.EXTRA_ID, -1);
        // id = -1 means, that we creating new sunblind, because existing has own id
        if (id != -1) {
            intent.putExtra(Utility.EXTRA_ID, id);
        }
        return intent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.name_editText || v.getId() == R.id.ip_editText) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return true;
        }
        return false;
    }
}
