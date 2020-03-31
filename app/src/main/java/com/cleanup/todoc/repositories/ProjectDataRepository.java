package com.cleanup.todoc.repositories;

import android.arch.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public LiveData<Project> getProject(long projectId) {
        return this.projectDao.getProjectById(projectId);
    }

    public void InitializeProjectInDatabase() {
        for (Project project : Project.getAllProjects()) {
            projectDao.createProject(project);
        }

    }
}
