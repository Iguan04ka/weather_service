package ru.iguana.weatherservicespringboot.api.controller.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.iguana.weatherservicespringboot.data.annotation.RussianLettersOnly;

import java.util.regex.Pattern;

public class RussianLettersOnlyValidator implements ConstraintValidator<RussianLettersOnly, String> {

    private static final Pattern RUSSIAN_LETTERS_PATTERN =
            Pattern.compile("^[А-Яа-яЁё]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return RUSSIAN_LETTERS_PATTERN.matcher(value).matches();
    }
}
