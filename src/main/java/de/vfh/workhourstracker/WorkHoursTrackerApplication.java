package de.vfh.workhourstracker;

import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkHoursTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkHoursTrackerApplication.class, args);
		EventLogger.logInfo("Work Hours Tracker Application started");
	}
}
