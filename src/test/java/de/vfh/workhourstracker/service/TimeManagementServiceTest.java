package de.vfh.workhourstracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TimeManagementServiceTest {

    @Autowired
    private TimeManagementService timeManagementService;

    //region Test validateStartTime
    @Test
    public void validateStartTime_ValidFormat_ReturnLocalDateTime() {
        String testStartTime = "2024-11-11T08:00:00";
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNotNull(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        Assertions.assertNotNull(formattedDateTime);
        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 8, 0, 0), localDateTime);
    }

    @Test
    public void validateStartTime_InvalidFormat_ReturnNull() {
        String testStartTime = "2024.11.11T8:00:00";
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void validateStartTime_StartTimeInFuture_ReturnNull() {
        String testStartTime = "2025-10-11T08:00:00";
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateEndTime
    @Test
    public void validateEndTime_ValidFormat_ReturnLocalDateTime() {
        String testEndTime = "2024-11-11T09:00:00";
        String testStartTime = "2024-11-11T08:00:00";
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNotNull(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        Assertions.assertNotNull(formattedDateTime);
        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 9, 0, 0), localDateTime);
    }

    @Test
    public void validateEndTime_EndTimeEqualsStartTime_ReturnNull() {
        String testStartTime = "2024-11-11T08:00:00";
        String testEndTime = "2024-11-11T08:00:00";
        LocalDateTime result = timeManagementService.validateEndTime(testStartTime, testEndTime);
        Assertions.assertNull(result, "Die Endzeit darf nicht gleich der Startzeit sein und sollte null zurückgeben.");
    }

    @Test
    public void validateEndTime_InvalidFormat_ReturnNull() {
        String testEndTime = "2024.11.11T8:00:00";
        String testStartTime = "2024-11-11T08:00:00";
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNull(localDateTime);
    }

    @Test
    public void validateEndTime_EndTimeInFuture_ReturnNull() {
        String testEndTime = "2025-10-11T08:00:00";
        String testStartTime = "2024-11-11T08:00:00";
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateDuration
    @Test
    public void validateDuration_ValidFormat_ReturnDuration() {
        String testDuration = "PT2H30M30S";
        Duration duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNotNull(duration);
        Assertions.assertEquals(9030L, duration.toSeconds());
    }

    @Test
    public void validateDuration_InvalidFormat_ReturnNull() {
        String testDuration = "02:30:30";
        Duration duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNull(duration);
    }

    @Test
    public void validateDuration_DurationTooLong_ReturnNull() {
        String testDuration = "PT48H30M30S";
        Duration duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNull(duration);
    }
    //endregion
}
