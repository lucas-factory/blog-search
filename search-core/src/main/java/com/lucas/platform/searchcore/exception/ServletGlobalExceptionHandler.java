package com.lucas.platform.searchcore.exception;

import com.lucas.platform.searchcore.config.CoreProperties;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.function.Function;

@Slf4j
@RestControllerAdvice
public class ServletGlobalExceptionHandler extends AbstractExceptionAttributes {

    private final Function<Throwable, ErrorResponse.Builder> responseBuilder;

    public ServletGlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor, CoreProperties properties) {
        super(messageSourceAccessor);
        this.responseBuilder = responseBuilder(properties);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(BssException exception) {
        String[] errorCodes = getBssExceptionErrorCode(exception);
        exception.printStackTrace();
        MessageSourceResolvable resolvable = new DefaultMessageSourceResolvable(errorCodes, exception.getErrorCode());
        String message = messageSourceAccessor.getMessage(resolvable);
        String localizedMessage = StringUtils.hasText(message) && !message.equals(exception.getErrorCode()) ? message : exception.getMessage();
        ErrorResponse response = responseBuilder.apply(exception)
                .errorCode(exception.getErrorCode())
                .text(localizedMessage)
                .build();
        return new ResponseEntity<>(response, exception.getStatus());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(BindException exception) {
        log.warn("BindException", exception);
        List<ErrorResponse.FieldMessageError> fieldErrors = getErrorMessageList(exception);
        String text = messageSourceAccessor.getMessage("BssException.INVALID_REQUEST");
        return responseBuilder.apply(exception)
                .errorCode(BssErrorCode.INVALID_REQUEST.name())
                .text(text)
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(WebExchangeBindException exception) {
        log.warn("WebExchangeBindException", exception);
        List<ErrorResponse.FieldMessageError> fieldErrors = getErrorMessageList(exception);
        String text = messageSourceAccessor.getMessage("BssException.INVALID_REQUEST");
        return responseBuilder.apply(exception)
                .errorCode(BssErrorCode.INVALID_REQUEST.name())
                .text(text)
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandler(ConstraintViolationException exception) {
        log.warn("ConstraintViolationException : {}", exception.getConstraintViolations());
        List<ErrorResponse.FieldMessageError> fieldErrors = getErrorMessageList(exception.getConstraintViolations().iterator());
        String text = messageSourceAccessor.getMessage("BssException.INVALID_REQUEST");
        return responseBuilder.apply(exception)
                .errorCode(BssErrorCode.INVALID_REQUEST.name())
                .text(text)
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(Throwable exception) {
        log.error("Unhandled Exception", exception);
        return responseBuilder.apply(exception)
                .errorCode(BssErrorCode.INTERNAL_SERVER_ERROR.name())
                .text(exception.getMessage())
                .build();
    }

    private Function<Throwable, ErrorResponse.Builder> responseBuilder(CoreProperties properties) {
        return throwable -> ErrorResponse.builder(properties.getProductId(), properties.getModuleId(), throwable);
    }

}
