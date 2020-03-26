package com.cleanup.todoc.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class CleanUpDatabase extends RoomDatabase {

    // SINGLETON
    private static volatile CleanUpDatabase INSTANCE;

    // DAOs
    public abstract TaskDao taskDao();

    public abstract ProjectDao projectDao();

    // INSTANCE
    public static CleanUpDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CleanUpDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CleanUpDatabase.class, "MyDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
