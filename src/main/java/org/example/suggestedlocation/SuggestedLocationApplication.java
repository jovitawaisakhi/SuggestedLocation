package org.example.suggestedlocation;

import org.example.suggestedlocation.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SuggestedLocationApplication{
	@Autowired
	private LocationService locationService;

	public static void main(String[] args) {
		SpringApplication.run(SuggestedLocationApplication.class, args);
	}

}
