package com.lucas.platform.searchcore.annotation;

import com.lucas.platform.searchcore.config.CoreProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Set;

public class SortTypeValidator implements ConstraintValidator<SortType, String> {

    private final Set<String> sortTypes;

    public SortTypeValidator(CoreProperties coreProperties) {
        this.sortTypes = coreProperties.getValidation().getSortTypes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return true;
        }
        return sortTypes.contains(value);
    }

}
