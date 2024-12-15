package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.shared.util.EventLogger;
import de.vfh.workhourstracker.usermanagement.application.services.UserService;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@SpringBootTest
public class TaskManagementServiceTest {

    @Autowired
    private TaskManagementService taskManagementService;
    EventLogger eventLogger = new EventLogger();
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectManagementService projectManagementService;

    //region Test createTask
    @Test
    public void createTask_ValidInput_ReturnTask() {
//        User user = userService.createUser("John Doe", "john.doe@mail.de");
//        Assertions.assertNotNull(user);
//
//        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
//        Assertions.assertNotNull(project);

//        Task task = taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0));
//        Assertions.assertNotNull(task);
    }
    //endregion

    //region Test updateTask
    public void updateTask_ValidInput_ReturnTask() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);


        ResponseEntity<?>task = taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0));
        Assertions.assertNotEquals(HttpStatus.UNPROCESSABLE_ENTITY, task.getStatusCode());
//        task = taskManagementService.updateTask(task.getTask_id(), "Fenster putzen", "Die Fenster müssen wirklich dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0));
//        Assertions.assertNotNull(task);
    }
    //endregion

    //region Test validateName
    @Test
    public void validateName_ValidateName_ReturnString() {
        String testName = "Test Name";
        String validName = taskManagementService.validateName(testName);
        Assertions.assertEquals( 0,validName.length());

    }

    @Test
    public void validateName_NameToLong_ReturnNull() {
        String testName = "tooLongTestName Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
                "et ea rebum. Stet clita kasd gubergren, no sea takimata ";
        String invalidName = taskManagementService.validateName(testName);
        Assertions.assertNotEquals( 0,invalidName.length());


    }
    //endregion

    //region Test validateDescription
    @Test
    public void validateDescription_ValidDescription_ReturnString() {
        String testDescription = "This is a test description";
        String validDescription = taskManagementService.validateDescription(testDescription);
        Assertions.assertEquals( 0,validDescription.length());


    }

    @Test
    public void validateDescription_DescriptionToLong_ReturnNull() {
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
        String invalidDescription = taskManagementService.validateDescription(testDescription);
        Assertions.assertNotEquals( 0,invalidDescription.length());
    }
    //endregion

    //region Test validateDeadline
    @Test
    public void validateDeadline_ValidDeadline_ReturnLocalDateTime() {
        LocalDateTime testDeadline = LocalDateTime.of(2025, 12, 10, 23, 59, 59);
        String validDeadline = taskManagementService.validateDeadline(testDeadline);

        Assertions.assertEquals( 0,validDeadline.length());

//        Assertions.assertNotNull(validDeadline);
//        Assertions.assertEquals(testDeadline, validDeadline);
    }

    @Test
    public void validateDeadline_DeadlineInThePast_ReturnNull() {
        LocalDateTime testDeadline = LocalDateTime.of(2024, 3, 10, 23, 59, 59);
        String invalidDeadline = taskManagementService.validateDeadline(testDeadline);

        Assertions.assertNotEquals( 0,invalidDeadline.length());
    }
    //endregion
}
