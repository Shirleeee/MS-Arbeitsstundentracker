package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.common.ProjectManagementValidationUtils;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.shared.util.EventLogger;
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
 class ProjectManagementServiceTest {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private UserService userService;
    @Autowired
    private TaskManagementService taskManagementService;
    @Autowired
    private TimeManagementService timeManagementService;

    //region Test createProject
    @Test
     void createProject_ValidInput_ReturnProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);
    }
    //endregion

    //region Test updatedProject
    @Test
     void updateProject_ValidInput_ReturnProject() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        project = (Project) projectManagementService.updateProject(project.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 1, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);
    }
    //endregion

    //region Test getTotalDurationOfProject
    @Test
     void getTotalDurationOfProject_ValidInput_ReturnDurationOfProject() {
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
EventLogger eventLogger = new EventLogger();
    //region Test validateName
    @Test
     void validateName_ValidName_ReturnString() {
        String testName = "Test Name";
        String validName = ProjectManagementValidationUtils.validateName(testName,  eventLogger);
        Assertions.assertEquals("", validName);
    }

    @Test
     void validateName_NameEmpty_ReturnString() {
        String testName = "";
        String invalidName = ProjectManagementValidationUtils.validateName(testName,  eventLogger);
        Assertions.assertEquals("Name darf nicht leer sein.", invalidName);
    }

    @Test
     void validateName_NameToLong_ReturnString() {
        String testName = "tooLongTestName Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
                "et ea rebum. Stet clita kasd gubergren, no sea takimata ";
        String invalidName = ProjectManagementValidationUtils.validateName(testName,  eventLogger);
        Assertions.assertEquals("Name ist zu lang.", invalidName);
    }
    //endregion

    //region Test validateDescription
    @Test
     void validateDescription_ValidDescription_ReturnString() {
        String testDescription = "This is a test description";
        String validDescription = ProjectManagementValidationUtils.validateDescription(testDescription,  eventLogger);
        Assertions.assertEquals("", validDescription);
    }

    @Test
     void validateDescription_DescriptionToLong_ReturnString() {
        String testDescription = "This is a way too long test description. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor " +
                "sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore " +
                "et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita " +
                "kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur " +
                "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est " +
                "Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, " +
                "vel illum dolore eu feugiat nulla facilisis a";
        String invalidDescription = ProjectManagementValidationUtils.validateDescription(testDescription,  eventLogger);
        Assertions.assertEquals("Beschreibung ist zu lang.", invalidDescription);
    }
    //endregion

    //region Test validateDeadline
    @Test
     void validateDeadline_ValidDeadline_ReturnString() {
        LocalDateTime testDeadline = LocalDateTime.of(2025, 12, 10, 23, 59, 59);
        String validDeadline = ProjectManagementValidationUtils.validateDeadline(testDeadline,  eventLogger);
        Assertions.assertEquals("", validDeadline);
    }

    @Test
     void validateDeadline_DeadlineEmpty_ReturnString() {
        String invalidDeadline = ProjectManagementValidationUtils.validateDeadline(null,  eventLogger);
        Assertions.assertEquals("Deadline darf nicht leer sein.", invalidDeadline);
    }

    @Test
     void validateDeadline_DeadlineInThePast_ReturnString() {
        LocalDateTime testDeadline = LocalDateTime.of(2024, 3, 10, 23, 59, 59);
        String invalidDeadline = ProjectManagementValidationUtils.validateDeadline(testDeadline,  eventLogger);
        Assertions.assertEquals("Deadline darf nicht in der Vergangenheit liegen.", invalidDeadline);
    }

    @Test
     void validateDeadline_DeadlineTooFarInFuture_ReturnString() {
        LocalDateTime testDeadline = LocalDateTime.of(2999, 12, 10, 23, 59, 59);
        String invalidDeadline = ProjectManagementValidationUtils.validateDeadline(testDeadline,  eventLogger);
        Assertions.assertEquals("Deadline darf nicht nach dem 31.12.2100 liegen.", invalidDeadline);
    }
    //endregion
}
