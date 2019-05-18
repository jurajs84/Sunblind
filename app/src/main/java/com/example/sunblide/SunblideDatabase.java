package com.example.sunblide;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Sunblide.class, version = 1)
public abstract class SunblideDatabase extends RoomDatabase {

    public abstract SunblideDao sunblideDao();

    private static volatile SunblideDatabase INSTANCE;

    static SunblideDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SunblideDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SunblideDatabase.class, "sunblide_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
