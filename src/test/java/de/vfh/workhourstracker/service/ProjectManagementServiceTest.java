package de.vfh.workhourstracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectManagementServiceTest {

    @Autowired
    private ProjectManagementService projectManagementService;

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
    public void validateDeadline_ValidDeadline_ReturnLocalDate() {
        String testDeadline = "2025-10-12T23:59:59";
        LocalDate localDate = projectManagementService.validateDeadline(testDeadline);
        Assertions.assertNotNull(localDate);
        //Assertions.assertEquals("", localDate);
    }

    @Test
    public void validateDeadline_DeadlineInThePast_ReturnNull() {
        String testDeadline = "2023-12-10T23:59:59";
        LocalDate localDate = projectManagementService.validateDeadline(testDeadline);
        Assertions.assertNull(localDate);
    }

    @Test
    public void validateDeadline_InvalidFormat_ReturnNull() {
        String testDeadline = "10.12.2025";
        LocalDate localDate = projectManagementService.validateDeadline(testDeadline);
        Assertions.assertNull(localDate);
    }
    //endregion
}
