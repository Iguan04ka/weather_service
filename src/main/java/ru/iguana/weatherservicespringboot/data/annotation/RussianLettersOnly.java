package ru.iguana.weatherservicespringboot.data.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.iguana.weatherservicespringboot.api.controller.validator.RussianLettersOnlyValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RussianLettersOnlyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RussianLettersOnly {
    String message() default "must contain only Russian letters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
