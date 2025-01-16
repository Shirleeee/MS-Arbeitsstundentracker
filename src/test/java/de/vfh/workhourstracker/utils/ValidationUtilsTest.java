package de.vfh.workhourstracker.utils;

import de.vfh.workhourstracker.projectmanagement.utils.ValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootTest
public class ValidationUtilsTest {
    private static final String VALID_NAME = "Test Name";
    private static final String VALID_DESCRIPTION = "Test Description";
    private static final LocalDateTime VALID_DEADLINE = LocalDateTime.of(2025, 12, 31, 23, 59, 59);

    @Test
    public void validateObject_ValidParameters_ReturnNull() {
        ResponseEntity<?> response = ValidationUtils.validateObject(VALID_NAME, VALID_DESCRIPTION, VALID_DEADLINE);
        Assertions.assertNull(response);
    }

    //region validateName
    @Test
    public void validateObject_NameEmpty_ReturnResponseEntity() {
        ResponseEntity<?> response = ValidationUtils.validateObject(null, VALID_DESCRIPTION, VALID_DEADLINE);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void validateObject_NameTooLong_ReturnResponseEntity() {
        String testName = "tooLongTestName Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
                "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores " +
                "et ea rebum. Stet clita kasd gubergren, no sea takimata";
        ResponseEntity<?> response = ValidationUtils.validateObject(testName, VALID_DESCRIPTION, VALID_DEADLINE);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
    //endregion

    //region validateDescription
    //TODO: description empty?

    @Test
    public void validateObject_DescriptionTooLong_ReturnResponseEntity() {
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
        ResponseEntity<?> response = ValidationUtils.validateObject(VALID_NAME, testDescription, VALID_DEADLINE);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
    //endregion

    //region validateDeadline
    @Test
    public void validateObject_DeadlineEmpty_ReturnResponseEntity() {
        ResponseEntity<?> response = ValidationUtils.validateObject(VALID_NAME, VALID_DESCRIPTION, null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void validateObject_DeadlineInPast_ReturnResponseEntity() {
        LocalDateTime testDeadline = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        ResponseEntity<?> response = ValidationUtils.validateObject(VALID_NAME, VALID_DESCRIPTION, testDeadline);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void validateObject_DeadlineTooFarInFuture_ReturnResponseEntity() {
        LocalDateTime testDeadline = LocalDateTime.of(2999, 12, 31, 23, 59, 59);
        ResponseEntity<?> response = ValidationUtils.validateObject(VALID_NAME, VALID_DESCRIPTION, testDeadline);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
    //endregion
}
