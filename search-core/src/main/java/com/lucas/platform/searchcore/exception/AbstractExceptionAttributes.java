package com.lucas.platform.searchcore.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.*;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractExceptionAttributes {

    protected MessageSourceAccessor messageSourceAccessor;

    private final MessageCodesResolver messageResolver = new DefaultMessageCodesResolver();

    public AbstractExceptionAttributes(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    protected String[] getBssExceptionErrorCode(BssException exception) {
        String[] errorCodes = messageResolver.resolveMessageCodes(exception.getClass().getSimpleName(), exception.getErrorCode());

        if(exception.getClass().isAssignableFrom(BssException.class)) {
            return errorCodes;
        }

        String[] bssErrorCodes = messageResolver.resolveMessageCodes(BssException.class.getSimpleName(), exception.getErrorCode());
        List<String> errorCodeList = new ArrayList<>();
        errorCodeList.addAll(Arrays.asList(errorCodes));
        errorCodeList.addAll(Arrays.asList(bssErrorCodes));
        errorCodeList.sort((v1, v2) -> (int) v2.chars().filter(e -> e == '.').count() - (int) v1.chars().filter(e -> e == '.').count());
        return errorCodeList.toArray(new String[0]);
    }

    protected List<ErrorResponse.FieldMessageError> getErrorMessageList(BindingResult bindingResult) {
        List<? extends ObjectError> objectErrors = bindingResult.getFieldErrors().isEmpty() ? bindingResult.getAllErrors() : bindingResult.getFieldErrors();

        List<ErrorResponse.FieldMessageError> fieldMessageErrors = new ArrayList<>();

        for(ObjectError objectError : objectErrors) {
            ErrorResponse.FieldMessageError fieldMessageError = new ErrorResponse.FieldMessageError();
            if(objectError instanceof FieldError fieldError) {
                fieldMessageError.setField(fieldError.getField());
                fieldMessageError.setObject(fieldError.getObjectName());
                fieldMessageError.setReason(messageSourceAccessor.getMessage(fieldError));
                fieldMessageErrors.add(fieldMessageError);
            }
        }

        return fieldMessageErrors;
    }

    protected List<ErrorResponse.FieldMessageError> getErrorMessageList(Iterator<ConstraintViolation<?>> violationIterator) {
        List<ErrorResponse.FieldMessageError> fieldMessageErrors = new ArrayList<>();

        while (violationIterator.hasNext()) {
            ConstraintViolation<?> constraintViolation = violationIterator.next();
            ErrorResponse.FieldMessageError fieldMessageError = new ErrorResponse.FieldMessageError();
            String[] propertiesPath = constraintViolation.getPropertyPath().toString().split("\\.");
            if(propertiesPath.length > 1) {
                fieldMessageError.setObject(propertiesPath[0]);
                fieldMessageError.setField(propertiesPath[1]);
            }

            try {
                fieldMessageError.setReason(messageSourceAccessor.getMessage(constraintViolation.getPropertyPath().toString()));
            } catch (NoSuchMessageException e) {
                fieldMessageError.setReason(constraintViolation.getMessage());
            }
            fieldMessageErrors.add(fieldMessageError);
        }

        return fieldMessageErrors;
    }

}
