package de.vfh.workhourstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkHoursTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkHoursTrackerApplication.class, args);
		System.out.println("Work Hours Tracker Application started");
		System.out.println("Testing CI-Pipeline...");
	}

}
