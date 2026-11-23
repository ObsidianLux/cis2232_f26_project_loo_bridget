package ca.hccis.softball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the application. The annotation turns on component scanning (finding other classes), auto-configuration, and configuration support.
 */
@SpringBootApplication
public class TheApplication {
	public static void main(String[] args) {
		SpringApplication.run(TheApplication.class, args);
	}
}
