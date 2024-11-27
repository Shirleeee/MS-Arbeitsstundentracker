package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TaskManagementServiceTest {

    @Autowired
    private TaskManagementService taskManagementService;

    //region Test validateName
    @Test
    public void validateName_ValidateName_ReturnString() {
        String testName = "Test Name";
        String validName = taskManagementService.validateName(testName);
        Assertions.assertNotNull(validName);
        Assertions.assertEquals(validName, testName);
    }

    @Test
    public void validateName_NameToLong_ReturnNull() {
        String testName = "tooLongTestName Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
                "et ea rebum. Stet clita kasd gubergren, no sea takimata ";
        String invalidName = taskManagementService.validateName(testName);
        Assertions.assertNull(invalidName);
    }
    //endregion

    //region Test validateDescription
    @Test
    public void validateDescription_ValidDescription_ReturnString() {
        String testDescription = "This is a test description";
        String validDescription = taskManagementService.validateDescription(testDescription);
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
        String invalidDescription = taskManagementService.validateDescription(testDescription);
        Assertions.assertNull(invalidDescription);
    }
    //endregion

    //region Test validateDeadline
    @Test
    public void validateDeadline_ValidDeadline_ReturnLocalDateTime() {
        LocalDateTime testDeadline = LocalDateTime.of(2025, 12, 10, 23, 59, 59);
        LocalDateTime validDeadline = taskManagementService.validateDeadline(testDeadline);
        Assertions.assertNotNull(validDeadline);
        Assertions.assertEquals(testDeadline, validDeadline);
    }

    @Test
    public void validateDeadline_DeadlineInThePast_ReturnNull() {
        LocalDateTime testDeadline = LocalDateTime.of(2024, 3, 10, 23, 59, 59);
        LocalDateTime invalidDeadline = taskManagementService.validateDeadline(testDeadline);
        Assertions.assertNull(invalidDeadline);
    }
    //endregion
}
