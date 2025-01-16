package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.projectmanagement.application.services.ProjectManagementService;
import de.vfh.workhourstracker.projectmanagement.application.services.TaskManagementService;
import de.vfh.workhourstracker.projectmanagement.domain.project.Project;
import de.vfh.workhourstracker.projectmanagement.domain.task.Task;
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

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        Task task = (Task) taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0)).getBody();
        Assertions.assertNotNull(task);

        TimeEntry timeEntry = (TimeEntry) timeManagementService.startTimeTracking(task.getTask_id(), LocalDateTime.of(2024, 12, 1, 8, 0, 0)).getBody();
        Assertions.assertNotNull(timeEntry);
    }
    //endregion

    //region Test endTimeTracking
    @Test
    public void endTimeTracking_ValidInput_ReturnTimeEntry() {
        User user = userService.createUser("John Doe", "john.doe@mail.de");
        Assertions.assertNotNull(user);

        Project project = (Project) projectManagementService.createProject(user.getId(), "Hausputz", "Das Haus muss gründlich geputzt werden.", LocalDateTime.of(2025, 2, 15, 19, 0, 0)).getBody();
        Assertions.assertNotNull(project);

        Task task = (Task) taskManagementService.createTask(project.getId(), "Fenster putzen", "Die Fenster müssen dringend geputzt werden.", LocalDateTime.of(2025, 1, 30, 19, 0, 0)).getBody();
        Assertions.assertNotNull(task);

        TimeEntry timeEntry = (TimeEntry) timeManagementService.startTimeTracking(task.getTask_id(), LocalDateTime.of(2024, 12, 1, 8, 0, 0)).getBody();
        Assertions.assertNotNull(timeEntry);

        timeEntry = (TimeEntry) timeManagementService.endTimeTracking(timeEntry.getId(), LocalDateTime.of(2024, 12, 1, 9, 0, 0)).getBody();
        Assertions.assertNotNull(timeEntry);
    }
    //endregion


    //region Test validateStartTime
    @Test
    public void validateStartTime_ValidFormat_ReturnEmptyString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String validStartTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertEquals("", validStartTime);
    }

    @Test
    public void validateStartTime_StartTimeEmpty_ReturnErrorMessageString() {
        String invalidStartTime = timeManagementService.validateStartTime(null);
        Assertions.assertEquals("Startzeitpunkt darf nicht leer sein.", invalidStartTime);
    }

    @Test
    public void validateStartTime_StartTimeInFuture_ReturnErrorMessageString() {
        LocalDateTime testStartTime = LocalDateTime.of(2025, 11, 10, 8, 0, 0);
        String invalidStartTime = timeManagementService.validateStartTime(testStartTime);
        Assertions.assertEquals("Startzeitpunkt liegt in der Zukunft.", invalidStartTime);
    }
    //endregion

    //region Test validateEndTime
    @Test
    public void validateEndTime_ValidFormat_ReturnEmptyString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 9, 0, 0);
        String validEndTime = timeManagementService.validateEndTime(testEndTime, testStartTime);
        Assertions.assertEquals("", validEndTime);
    }

    @Test
    public void validateEndTime_EndTimeEmpty_ReturnErrorMessageString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String invalidEndTime = timeManagementService.validateEndTime(null, testStartTime);
        Assertions.assertEquals("Endzeitpunkt darf nicht leer sein.", invalidEndTime);
    }

    @Test
    public void validateEndTime_EndTimeInFuture_ReturnErrorMessageString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2025, 11 ,10, 8, 0, 0);
        String invalidEndTime = timeManagementService.validateEndTime(testEndTime, testStartTime);
        Assertions.assertEquals("Endzeitpunkt liegt in der Zukunft.", invalidEndTime);
    }

    @Test
    public void validateEndTime_EndTimeEqualsStartTime_ReturnErrorMessageString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024,11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        String invalidEndTime = timeManagementService.validateEndTime(testStartTime, testEndTime);
        Assertions.assertEquals("Endzeitpunkt ist gleich Startzeitpunkt.", invalidEndTime);
    }

    @Test
    public void validateEndTime_EndTimeBeforeStartTime_ReturnErrorMessageString() {
        LocalDateTime testStartTime = LocalDateTime.of(2024, 11, 11, 8, 0, 0);
        LocalDateTime testEndTime = LocalDateTime.of(2024, 11, 11, 5, 0, 0);
        String invalidEndTime = timeManagementService.validateEndTime(testEndTime, testStartTime);
        Assertions.assertEquals("Endzeitpunkt liegt vor Startzeitpunkt.", invalidEndTime);
    }
    //endregion

    //region Test validateDuration
    @Test
    public void validateDuration_ValidFormat_ReturnEmptyString() {
        Duration testDuration = Duration.parse("PT2H30M30S");
        String duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertEquals("", duration);
    }

    @Test
    public void validateDuration_DurationEmpty_ReturnErrorMessageString() {
        String invalidDuration = timeManagementService.validateDuration(null);
        Assertions.assertEquals("Dauer darf nicht leer sein.", invalidDuration);
    }

    @Test
    public void validateDuration_DurationTooLong_ReturnErrorMessageString() {
        Duration testDuration = Duration.parse("PT48H30M30S");
        String duration = timeManagementService.validateDuration(testDuration);
        Assertions.assertEquals("Dauer ist zu lang.", duration);
    }
    //endregion
}
