package ru.iguana.weatherservicespringboot.data.exception;

public class CityNotFoundException extends RuntimeException{

    public CityNotFoundException(String message) {
        super("Город с названием " + message + " не найден");
    }
}
