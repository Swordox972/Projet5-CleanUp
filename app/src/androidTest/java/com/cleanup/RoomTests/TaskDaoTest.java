package com.cleanup.RoomTests;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.CleanUpDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private CleanUpDatabase database;

    // DATA FOR TEST
    private static long PROJECT_ID = 4;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Projet Lol", Color.blue(255));

    // TASKS FOR TEST
    private static Task task = new Task(1, PROJECT_ID, "Faire le ménage", 1);
    private static Task task2 = new Task(2, PROJECT_ID, "Faire la vaisselle", 1);
    private static Task task3 = new Task(3, PROJECT_ID, "Passer l'aspirateur", 2);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                CleanUpDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void close() throws Exception {
        this.database.close();
    }

    @Test
    public void getTasksShouldBeEmpty() throws InterruptedException{
        //TEST
        List<Task> tasks= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID));
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void getProjectByIdShouldWork() throws InterruptedException {
        //ADD MY DEMO PROJECT
        this.database.projectDao().createProject(PROJECT_DEMO);

        // GET PROJECT AND ASSERT THIS IS PROJECT DEMO
        Project project = LiveDataTestUtil.getValue(this.database.projectDao()
                .getProjectById(PROJECT_ID));
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) &&
                project.getColor() == PROJECT_DEMO.getColor());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
      // 1- ADD PROJECT DEMO AND 2-ADD MY DEMO TASKS
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(task);
        this.database.taskDao().insertTask(task2);
        this.database.taskDao().insertTask(task3);

        // GET LIST TASK AND ASSER SIZE == 3
        List<Task> tasks= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID));
        assertTrue(tasks.size() == 3);
    }

    @Test
    public void insertAndUpdateTasks() throws InterruptedException{
        // 1-ADD PROJECT DEMO AND TASK 2- READ TASK AND UPDATE IT
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(task);

        Task taskAdded= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID)).get(0);
         taskAdded.setName("Faire la lessive");
        this.database.taskDao().updateTask(taskAdded);

        //TEST
       List<Task> tasks= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID));
       assertTrue(!tasks.get(0).getName().equals(task.getName())
               && tasks.get(0).getName().equals("Faire la lessive"));

    }

    @Test
    public void insertAndDeleteTasks() throws InterruptedException {
        // ADD PROJECT, ADD TASK, RETRIEVE TASK AND DELETE IT
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(task2);
       Task taskAdded= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID)).get(0);
       this.database.taskDao().deleteTask(taskAdded);

       // TEST
        List<Task> tasks= LiveDataTestUtil.getValue(this.database.taskDao().getTasks(PROJECT_ID));
        assertTrue(tasks.isEmpty());


    }
}
