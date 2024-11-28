package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
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
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNotNull(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        Assertions.assertNotNull(formattedDateTime);
        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 8, 0, 0), localDateTime);
    }

    @Test
    public void validateStartTime_StartTimeInFuture_ReturnNull() {
        LocalDateTime testStartTime = LocalDateTime.of(2025, 11, 10, 8, 0, 0);
        LocalDateTime localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateEndTime
    @Test
    public void validateEndTime_ValidFormat_ReturnLocalDateTime() {
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 9, 0, 0);
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNotNull(localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        Assertions.assertNotNull(formattedDateTime);
        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 9, 0, 0), localDateTime);
    }

    @Test
    public void validateEndTime_EndTimeEqualsStartTime_ReturnNull() {
        LocalDateTime testStartTime = LocalDateTime.of(2024,11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime result = timeManagementService.validateEndTime(testStartTime, testEndTime);
        Assertions.assertNull(result, "Die Endzeit darf nicht gleich der Startzeit sein und sollte null zurückgeben.");
    }

    @Test
    public void validateEndTime_EndTimeInFuture_ReturnNull() {
        LocalDateTime testEndTime = LocalDateTime.of(2025, 11 ,10, 8, 0, 0);
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNull(localDateTime);
    }
    //endregion

    //region Test validateDuration
    @Test
    public void validateDuration_ValidFormat_ReturnDuration() {
        Duration testDuration = Duration.parse("PT2H30M30S");
        Duration duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNotNull(duration);
        Assertions.assertEquals(9030L, duration.toSeconds());
    }

    @Test
    public void validateDuration_DurationTooLong_ReturnNull() {
        Duration testDuration = Duration.parse("PT48H30M30S");
        Duration duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNull(duration);
    }
    //endregion
}
