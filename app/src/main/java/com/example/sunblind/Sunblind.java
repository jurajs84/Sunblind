package com.example.sunblind;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sunblind_table")
public class Sunblind {

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey (autoGenerate = true)
    private int id;

    private final String name;

    private final String address;

    private final int runningTime;

    private boolean isSelected;

    private boolean isOnline;

    public Sunblind(String name, String address, int runningTime) {
        this.name = name;
        this.address = address;
        this.runningTime = runningTime;
        //this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
