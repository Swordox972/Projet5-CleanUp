package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    LiveData<Project> currentProject;

    public TaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataSource = taskDataRepository;
        this.projectDataSource = projectDataRepository;
        this.executor = executor;
    }

    public void init(long projectId) {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject(projectId);
    }

    // FOR PROJECT
    public LiveData<Project> getProject(long projectId) {
        return this.currentProject;
    }

    // FOR TASK
    public LiveData<List<Task>> getTasks(long projectId) {
        return taskDataSource.getTasks(projectId);
    }

    public void createTask(Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void updateTask(Task task) {
        executor.execute(() -> taskDataSource.updateTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskDataSource.deleteTask(task));
    }
}
