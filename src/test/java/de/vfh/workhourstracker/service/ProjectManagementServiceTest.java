package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.usermanagement.application.services.UserService;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ProjectManagementServiceTest {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private UserService userService;

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

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        project = projectManagementService.updateProject(project.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 1, 15, 19, 0, 0));
        Assertions.assertNotNull(project);
    }
    //endregion

    //region Test validateName
    @Test
    public void validateName_ValidName_ReturnString() {
        String testName = "Test Name";
        String validName = projectManagementService.validateName(testName);
        Assertions.assertNotNull(validName);
        Assertions.assertEquals(validName, testName);
    }

    @Test
    public void validateName_NameToLong_ReturnNull() {
        String testName = "tooLongTestName Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
                "et ea rebum. Stet clita kasd gubergren, no sea takimata ";
        String invalidName = projectManagementService.validateName(testName);
        Assertions.assertNull(invalidName);
    }
    //endregion

    //region Test validateDescription
    @Test
    public void validateDescription_ValidDescription_ReturnString() {
        String testDescription = "This is a test description";
        String validDescription = projectManagementService.validateDescription(testDescription);
        Assertions.assertNotNull(validDescription);
        Assertions.assertEquals(testDescription, validDescription);
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
        String invalidDescription = projectManagementService.validateDescription(testDescription);
        Assertions.assertNull(invalidDescription);
    }
    //endregion

    //region Test validateDeadline
    @Test
    public void validateDeadline_ValidDeadline_ReturnLocalDateTime() {
        LocalDateTime testDeadline = LocalDateTime.of(2025, 12, 10, 23, 59, 59);
        LocalDateTime validDeadline = projectManagementService.validateDeadline(testDeadline);
        Assertions.assertNotNull(validDeadline);
        Assertions.assertEquals(testDeadline, validDeadline);
    }

    @Test
    public void validateDeadline_DeadlineInThePast_ReturnNull() {
        LocalDateTime testDeadline = LocalDateTime.of(2024, 3, 10, 23, 59, 59);
        LocalDateTime invalidDeadline = projectManagementService.validateDeadline(testDeadline);
        Assertions.assertNull(invalidDeadline);
    }
    //endregion
}
