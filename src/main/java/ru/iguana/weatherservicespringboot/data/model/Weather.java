package ru.iguana.weatherservicespringboot.data.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class Weather {
    private int temperature;
    private int humidity;
    private int windSpeed;

    public Weather() {
        Random random = new Random();
        this.temperature = random.nextInt(-30, 50);
        this.humidity = random.nextInt(0, 100);
        this.windSpeed = random.nextInt(0, 30);
    }

    @Override
    public String toString() {
        return "Температура воздуха = " + temperature + "°C, " +
                "влажность = " + humidity + "%, " +
                "скорость ветра = " + windSpeed + " м/с";
    }
}

