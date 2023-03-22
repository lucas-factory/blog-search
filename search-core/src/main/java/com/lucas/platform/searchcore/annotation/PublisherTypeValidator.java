package com.lucas.platform.searchcore.annotation;

import com.lucas.platform.searchcore.config.ClientsProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Set;

@Slf4j
public class PublisherTypeValidator implements ConstraintValidator<PublisherType, String> {

    private final Set<String> publisherTypes;

    public PublisherTypeValidator(ClientsProperties clientsProperties) {
        this.publisherTypes = clientsProperties.getCredential().keySet();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("value : {}", value);
        if (!StringUtils.hasText(value)) {
            return false;
        }
        log.info("{} publisherType: {}", publisherTypes.contains(value), publisherTypes);

        return publisherTypes.contains(value);
    }
}
