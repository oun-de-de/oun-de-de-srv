package com.cdtphuhoi.oun_de_de.validators;

import com.cdtphuhoi.oun_de_de.common.ValueBasedEnum;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
            .map(e -> {
                if (ValueBasedEnum.class.isAssignableFrom(annotation.enumClass())) {
                    try {
                        var m = e.getClass().getMethod("getValue");
                        return m.invoke(e).toString();
                    } catch (Exception ex) {
                        // fallback to enum name
                        return e.name();
                    }
                }
                return e.name();
            })
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return acceptedValues.contains(value.toString());

    }
}
