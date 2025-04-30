package ru.iguana.weatherservicespringboot.data.exception;

public class IllegalCityNameException extends RuntimeException {
    public IllegalCityNameException(String cityName) {
        super("Некорректное название города: \"" + cityName + "\"");
    }
}
