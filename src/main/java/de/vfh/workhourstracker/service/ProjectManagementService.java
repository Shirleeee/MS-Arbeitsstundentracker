package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.Project;
import de.vfh.workhourstracker.entity.Task;
import de.vfh.workhourstracker.repository.ProjectRepository;
import de.vfh.workhourstracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectManagementService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectManagementService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;

    }


    public void createProject(Long userId, String name, String description, String deadline) {

//       validateProjectId(userId);
        validateName(name);
        validateDescription(description);
        LocalDateTime dateTimeDeadline = validateDeadline(deadline);

        Project newProject = new Project(userId, name, description, dateTimeDeadline);
        this.saveProject(newProject);
        //return Wert?
    }


    public void createTask(Long projectId, String name, String description, String deadline) {

        //  validateProjectId(projectId);
        validateName(name);
        validateDescription(description);
        LocalDateTime dateTimeDeadline = validateDeadline(deadline);

        Task newTask = new Task(projectId, name, description, dateTimeDeadline);
        this.saveTask(newTask);
        //return Wert?
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
        if (name == null || name.isEmpty()) {
            return null;
            //throw new IllegalArgumentException("Name darf nicht leer sein.");
        }

        return null;
    }


    public String validateDescription(String description) {
        //TODO
        if (description == null || description.isEmpty()) {
            return null;

        }


        return null;
    }

//    public void validateProjectId(Long projectId) {
//        //TODO
//      if (projectId == null || projectId < 0) {
//          throw new IllegalArgumentException("Keine valide id vorhanden.");
//      }
//        return null;
//    }

    //klären: welche Formate sollen als valide gelten?
    public LocalDateTime validateDeadline(String deadline) {
        //TODO
        if (deadline == null) {
            return null;
        } else {

            LocalDateTime dateTimeDeadline;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                dateTimeDeadline = LocalDateTime.parse(deadline, formatter);

            } catch (Exception e) {
                return null;
                // throw new IllegalArgumentException("Deadline hat falsches Format. Format: yyyy-MM-dd'T'HH:mm:ss");
            }

            try {
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(dateTimeDeadline)) {
                    return null;
                    // throw new IllegalArgumentException("Deadline liegt in der Vergangenheit.");
                }
            } catch (Exception e) {

                // throw new IllegalArgumentException("Deadline darf nicht in der Vergangenheit liegen.");
                return null;
            }
            return dateTimeDeadline;
        }


    }
}
