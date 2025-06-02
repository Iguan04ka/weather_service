package ru.iguana.weatherservicespringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;

@SpringBootApplication
@EnableScheduling
public class WeatherServiceSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeatherServiceSpringBootApplication.class, args);
	}

}
