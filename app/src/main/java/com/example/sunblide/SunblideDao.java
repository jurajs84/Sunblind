package com.example.sunblide;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SunblideDao {

    @Insert
    void insert(Sunblide sunblide);

    @Update
    void update(Sunblide sunblide);

    @Delete
    void delete(Sunblide sunblide);

    @Query("SELECT * FROM sunblide_table")
    LiveData<List<Sunblide>>getAllSunblides();
}
