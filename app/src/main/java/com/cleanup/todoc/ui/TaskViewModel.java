package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
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
   private LiveData<Project> listeningProject;
    @NonNull
    MediatorLiveData<Project> currentProject= new MediatorLiveData<>();

    @Nullable
   private LiveData<List<Task>> listeningTask;
    @NonNull
    MediatorLiveData<List<Task>> currentTasks= new MediatorLiveData<>();


    public TaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataSource = taskDataRepository;
        this.projectDataSource = projectDataRepository;
        this.executor = executor;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Project[] allProjects= Project.getAllProjects();
                for (Project project: allProjects) {
                    projectDataRepository.createProject(project);
                   selectProject(allProjects[0].getId());
                }
            }
        });
    }

    public void selectProject(long projectId) {
      //remove the old livedata listening

      if(listeningProject != null) {
          currentProject.removeSource(listeningProject);
      }
        //replace the old livedata listening
       listeningProject=projectDataSource.getProject(projectId);
       // listen to it
        currentProject.addSource(listeningProject,project -> currentProject.setValue(project));

        //remove the old livedata listening
       if(listeningTask!= null) {
           currentTasks.removeSource(listeningTask);

           //replace the old livedata listening
           listeningTask=taskDataSource.getTasks(projectId);

           //listen to it
           currentTasks.addSource(listeningTask,tasks -> currentTasks.setValue(tasks));
       }
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
