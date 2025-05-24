package ru.iguana.weatherservicespringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;

@SpringBootApplication
public class WeatherServiceSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeatherServiceSpringBootApplication.class, args);
	}

}
