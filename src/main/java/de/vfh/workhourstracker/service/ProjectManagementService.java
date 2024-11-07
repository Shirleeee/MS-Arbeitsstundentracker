package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.Project;
import de.vfh.workhourstracker.entity.Task;
import de.vfh.workhourstracker.repository.ProjectRepository;
import de.vfh.workhourstracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProjectManagementService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    //region project
    public Project findProject(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
    //endregion

    //region task
    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    //endregion

    public String validateName(String name) {
        //TODO
        return null;
    }

    public String validateDescription(String description) {
        //TODO
        return null;
    }

    public LocalDate validateDeadline(String deadline) {
        //TODO
        return null;
    }
}
