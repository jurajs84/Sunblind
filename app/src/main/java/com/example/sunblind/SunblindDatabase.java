package com.example.sunblind;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Sunblind.class, version = 1)
public abstract class SunblindDatabase extends RoomDatabase {

    public abstract SunblindDao sunblindDao();

    private static volatile SunblindDatabase INSTANCE;

    static SunblindDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SunblindDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SunblindDatabase.class, "sunblind_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
