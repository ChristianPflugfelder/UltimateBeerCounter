package de.haepfl.ultimatebeercounter.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Day.class, version = 1, exportSchema = false)
public abstract class DayDatabase extends RoomDatabase {

    private static DayDatabase instance;

    public abstract DayDao dayDao();

    public static synchronized DayDatabase getInstance(Context context) {
        if (instance == null) {
            //fallbackToDestructiveMigration deletes the old Database if the version is increased
            instance = Room.databaseBuilder(context.getApplicationContext(), DayDatabase.class, "day_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
