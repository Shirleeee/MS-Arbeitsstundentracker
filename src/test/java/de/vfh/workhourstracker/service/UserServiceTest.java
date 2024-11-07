package de.vfh.workhourstracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    //region Test validateName
    @Test
    public void validateName_ValidName_ReturnString() {}

    @Test
    public void validateName_InvalidChars_ReturnNull() {}

    @Test
    public void validateName_NameTooLong_ReturnNull() {}
    //endregion

    //region Test validateMailAddress
    @Test
    public void validateMailAddress_ValidEmail_ReturnString() {}

    @Test
    public void validateMailAddress_InvalidEmail_ReturnNull() {}
    //endregion
}
