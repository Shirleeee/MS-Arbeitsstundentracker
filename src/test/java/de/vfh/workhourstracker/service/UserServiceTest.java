package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.WorkHoursTrackerApplication;
import de.vfh.workhourstracker.usermanagement.application.services.UserService;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WorkHoursTrackerApplication.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    //region Test createUser
    @Test
    void createUser_validInput_ReturnUser() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);
    }
    //endregion

    //region Test validateName
    @Test
    void validateName_ValidName_ReturnString() {
        String testName = "Max Mustermann";
        String validName = userService.validateName(testName);
        Assertions.assertNotNull(validName);
        Assertions.assertEquals(validName, testName);
    }

    @Test
    void validateName_InvalidChars_ReturnNull() {
        String testName = "Max Mu§+€rmann";
        String invalidName = userService.validateName(testName);
        Assertions.assertNull(invalidName);
    }

    @Test
    void validateName_NameTooLong_ReturnNull() {
        String testName = "Maximilian Theodor Wilhelm Franz Benedikt Albrecht Mustermann-Schneider von und zu Schwabenhausen";
        String invalidName = userService.validateName(testName);
        Assertions.assertNull(invalidName);
    }
    //endregion

    //region Test validateMailAddress
    @Test
    void validateMailAddress_ValidEmail_ReturnString() {
        String testMailAddress = "test@test.com";
        String mailAddress = userService.validateMailAddress(testMailAddress);
        Assertions.assertNotNull(mailAddress);
        Assertions.assertEquals(testMailAddress, mailAddress);
    }

    @Test
    void validateMailAddress_InvalidEmail_ReturnNull() {
        String testMailAddress = "test/test-com";
        String mailAddress = userService.validateMailAddress(testMailAddress);
        Assertions.assertNull(mailAddress);
    }
    //endregion
}
