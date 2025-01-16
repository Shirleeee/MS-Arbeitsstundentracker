package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.projectmanagement.infrastructure.repositories.ProjectRepository;
import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.usermanagement.application.services.UserService;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
public class ProjectManagementServiceTest {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private UserService userService;
    @Autowired
    private TaskManagementService taskManagementService;
    @Autowired
    private TimeManagementService timeManagementService;
    @Autowired
    private ProjectRepository projectRepository;

    //region Test createProject
    @Test
    public void createProject_ValidInput_ReturnProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);
    }
    //endregion

    //region Test updatedProject
    @Test
    public void updateProject_ValidInput_ReturnProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 3, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        project = (Project) projectManagementService.updateProject(project.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);
    }
    //endregion

    //region Test deleteProject
    @Test
    public void deleteProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        projectManagementService.deleteProject(project.getId());
        Assertions.assertFalse(projectRepository.findById(project.getId()).isPresent());
    }
    //endregion

    //region Test getTotalDurationOfProject
    @Test
    public void getTotalDurationOfProject_ValidInput_ReturnDurationOfProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        Task task1 = (Task) taskManagementService.createTask(project.getId(), "Staubsaugen", "Es ist sehr dreckig", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(task1);

        TimeEntry timeEntry1 = (TimeEntry) timeManagementService.createTimeEntry(task1.getTask_id(), LocalDateTime.of(2025, 1, 1, 14, 0, 0), LocalDateTime.of(2025, 1, 1, 15, 0, 0)).getBody();
        Assertions.assertNotNull(timeEntry1);

        Task task2 = (Task) taskManagementService.createTask(project.getId(), "Fenster putzen", "Ich kann gar nicht mehr nach draußen gucken", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(task2);

        TimeEntry timeEntry2 = (TimeEntry) timeManagementService.createTimeEntry(task2.getTask_id(), LocalDateTime.of(2025, 1, 3, 12, 34, 15), LocalDateTime.of(2025, 1, 3, 13, 0, 0)).getBody();
        Assertions.assertNotNull(timeEntry2);

        Duration duration = projectManagementService.getTotalDurationOfProject(project.getId());
        Assertions.assertEquals(Duration.ofHours(1).plusMinutes(25).plusSeconds(45), duration);
    }
    //endregion
}
