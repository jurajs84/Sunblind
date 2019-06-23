package com.example.sunblind;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SunblindDao {

    @Insert
    void insert(Sunblind sunblind);

    @Update
    void update(Sunblind sunblind);

    @Delete
    void delete(Sunblind sunblind);

    @Query("SELECT * FROM sunblind_table")
    LiveData<List<Sunblind>> getAllSunblinds();
}
