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

    private String name;

    private String address;

    private int runningTime;

    public Sunblind(String name, String address, int runningTime) {
        this.name = name;
        this.address = address;
        this.runningTime = runningTime;
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
}
