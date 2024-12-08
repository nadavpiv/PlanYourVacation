package com.nadavpiv.vacation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.nio.file.Paths;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) // Excludes automatic configuration of the DataSource (i.e., no database configuration is provided by Spring Boot).
public class VacationApplication {

	public static void main(String[] args) {
		// Launches the Spring Boot application.
		SpringApplication.run(VacationApplication.class, args);
	}

}
