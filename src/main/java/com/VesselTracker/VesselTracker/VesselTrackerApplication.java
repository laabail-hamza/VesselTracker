package com.VesselTracker.VesselTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VesselTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VesselTrackerApplication.class, args);
	}

}
