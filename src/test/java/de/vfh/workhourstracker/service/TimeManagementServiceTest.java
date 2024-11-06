package de.vfh.workhourstracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class TimeManagementServiceTest {

    @Autowired
    private TimeManagementService timeManagementService;

    //region Test validateStartTime
    @Test
    public void validateStartTime_ValidFormat_ReturnLocalDateTime() {
        String testStartTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNotNull(localDateTime);
//        Assertions.assertEquals("", localDateTime);
    }

    @Test
    public void validateStartTime_InvalidFormat_ReturnNull() {
        String testStartTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void validateStartTime_StartTimeInFuture_ReturnNull() {
        String testStartTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateEndTime
    @Test
    public void validateEndTime_ValidFormat_ReturnLocalDateTime() {
        String testEndTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime);
        Assertions.assertNotNull(localDateTime);
//        Assertions.assertEquals("", localDateTime);
    }

    @Test
    public void validateEndTime_InvalidFormat_ReturnNull() {
        String testEndTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime);
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void validateEndTime_EndTimeInFuture_ReturnNull() {
        String testEndTime = ""; //klären: welche Formate sollen als valide gelten?
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateDuration
    //endregion
}
