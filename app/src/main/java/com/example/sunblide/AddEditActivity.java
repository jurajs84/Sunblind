package com.example.sunblide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText ipAddressEditText;
    FloatingActionButton checkFab;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);

        nameEditText = findViewById(R.id.name_editText);
        ipAddressEditText = findViewById(R.id.ip_editText);
        checkFab = findViewById(R.id.add_fab);

        intent = getIntent();
        if (intent.hasExtra(Utility.EXTRA_ID)){
            setTitle("Edit sunblide");
            //settings EditTexts to proper values
            nameEditText.setText(intent.getStringExtra(Utility.EXTRA_NAME));
            ipAddressEditText.setText(intent.getStringExtra(Utility.EXTRA_IP_ADDRESS));
        }
        else {
            setTitle("Add sunblide");
        }

        checkFab.setOnClickListener(view -> {
            saveSunblide();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (intent.hasExtra(Utility.EXTRA_ID)) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.delete_sunblide, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.delete_sunblide:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to delete it?")
                .setPositiveButton("OK", (dialog, which) -> deleteSunblide())
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void deleteSunblide() {
        String name = nameEditText.getText().toString();
        String ipAddress = ipAddressEditText.getText().toString();
        setResult(Utility.DELETE_SUNBLIDE, makeNewIntent(name, ipAddress));
        finish();
    }

    private void saveSunblide() {
        String name = nameEditText.getText().toString().trim();
        String ipAddress = ipAddressEditText.getText().toString().trim();

        if (name.isEmpty()){
            Toast.makeText(this, "Add name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ipAddress.isEmpty()){
            Toast.makeText(this, "Add IP address", Toast.LENGTH_SHORT).show();
            return;
        }
        //sending data to MainActivity via SunblideItemActivity activity

        setResult(RESULT_OK, makeNewIntent(name, ipAddress));
        finish();
    }

    private Intent makeNewIntent(String name, String ipAddress) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Utility.EXTRA_NAME, name);
        intent.putExtra(Utility.EXTRA_IP_ADDRESS, ipAddress);
        int id = getIntent().getIntExtra(Utility.EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(Utility.EXTRA_ID, id);
        }
        return intent;
    }

}
