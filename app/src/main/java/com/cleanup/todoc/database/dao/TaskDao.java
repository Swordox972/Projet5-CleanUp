package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    // CRUD

    // Create
    @Insert()
    long insertTask(Task task);

    // Read
    @Query("SELECT * FROM Task WHERE projectId= :projectId")
    LiveData<List<Task>> getTasksFromAProject(long projectId);

    // Read all Tasks whathever the project
    @Query("SELECT * FROM TASK")
    LiveData<List<Task>> getTasks();
    // Update
    @Update
    int updateTask(Task task);

    // Delete
    @Delete
    int deleteTask(Task task);

}
