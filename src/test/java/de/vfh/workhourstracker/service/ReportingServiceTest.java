package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.reporting.application.services.ReportingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
 class ReportingServiceTest {

    @Autowired
    private ReportingService reportingService;

    //region Test createReport
    @Test
     void createReport_ValidInput_ReturnByte() {
        //TODO
    }
    //endregion
}
