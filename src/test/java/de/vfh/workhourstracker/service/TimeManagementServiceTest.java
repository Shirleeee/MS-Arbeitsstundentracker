package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.project.ProjectId;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
import de.vfh.workhourstracker.timemanagement.application.services.TimeManagementService;
import de.vfh.workhourstracker.timemanagement.domain.timeentry.TimeEntry;
import de.vfh.workhourstracker.usermanagement.application.services.UserService;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TimeManagementServiceTest {

    @Autowired
    private TimeManagementService timeManagementService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private TaskManagementService taskManagementService;

    //region Test startTimeTracking
    @Test
    public void startTimeTracking_ValidInput_ReturnTimeEntry() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

//        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
//        Assertions.assertNotNull(project);
//
//        ResponseEntity<?> task = taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0));
//        Assertions.assertNotEquals(HttpStatus.UNPROCESSABLE_ENTITY, task.getStatusCode());

//        TimeEntry timeEntry = timeManagementService.startTimeTracking(task.getTask_id(), LocalDateTime.of(2024, 12, 1, 8, 0, 0));
//        Assertions.assertNotNull(timeEntry);
    }
    //endregion

    //region Test endTimeTracking
    public void endTimeTracking_ValidInput_ReturnTimeEntry() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

//        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
//        Assertions.assertNotNull(project);
//
//        ResponseEntity<?> task = taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0));
//        Assertions.assertNotEquals(HttpStatus.UNPROCESSABLE_ENTITY, task.getStatusCode());
//        Task task1 = (Task) task.getBody();
//        TimeEntry timeEntry = timeManagementService.startTimeTracking(task1.getTask_id(), LocalDateTime.of(2024, 12, 1, 8, 0, 0));
//        Assertions.assertNotNull(timeEntry);
//
//        timeEntry = timeManagementService.endTimeTracking(timeEntry.getId(), LocalDateTime.of(2024, 12, 1, 9, 0, 0));
//        Assertions.assertNotNull(timeEntry);
    }
    //endregion


    //region Test validateStartTime
    @Test
    public void validateStartTime_ValidFormat_ReturnLocalDateTime() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertEquals(0,localDateTime.length());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String formattedDateTime = localDateTime.format(formatter);
//        Assertions.assertNotNull(formattedDateTime);
//        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 8, 0, 0), localDateTime);
    }

    @Test
    public void validateStartTime_StartTimeInFuture_ReturnNull() {
        LocalDateTime testStartTime = LocalDateTime.of(2025, 11, 10, 8, 0, 0);
        String localDateTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertEquals("Startzeitpunkt liegt in der Zukunft.",localDateTime);
}
    //endregion

    //region Test validateEndTime
    @Test
    public void validateEndTime_ValidFormat_ReturnLocalDateTime() {
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 9, 0, 0);
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertEquals(0,localDateTime.length());

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String formattedDateTime = localDateTime.format(formatter);
//        Assertions.assertNotNull(formattedDateTime);
//        Assertions.assertEquals(LocalDateTime.of(2024, 11, 11, 9, 0, 0), localDateTime);
    }

    @Test
    public void validateEndTime_EndTimeEqualsStartTime_ReturnNull() {
        LocalDateTime testStartTime = LocalDateTime.of(2024,11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String result = timeManagementService.validateEndTime(testStartTime, testEndTime);
        Assertions.assertEquals("Endzeitpunkt liegt vor Startzeitpunkt.",result);

    }

    @Test
    public void validateEndTime_EndTimeInFuture_ReturnNull() {
        LocalDateTime testEndTime = LocalDateTime.of(2025, 11 ,10, 8, 0, 0);
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String localDateTime = timeManagementService.validateEndTime(testEndTime,testStartTime);
        Assertions.assertNotEquals(0,localDateTime.length());

    }
    //endregion

    //region Test validateDuration
    @Test
    public void validateDuration_ValidFormat_ReturnDuration() {
        Duration testDuration = Duration.parse("PT2H30M30S");
        String duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertEquals(0,duration.length());
    }

    @Test
    public void validateDuration_DurationTooLong_ReturnNull() {
        Duration testDuration = Duration.parse("PT48H30M30S");
        String duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertNotEquals(0,duration.length());
    }
    //endregion
}
