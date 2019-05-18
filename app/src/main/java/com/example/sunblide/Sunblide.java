package com.example.sunblide;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sunblide_table")
public class Sunblide {

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String name;

    private String address;

    public Sunblide(String name, String address) {
        this.name = name;
        this.address = address;
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
}
