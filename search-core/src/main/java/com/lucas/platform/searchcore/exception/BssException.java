package com.lucas.platform.searchcore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Slf4j
public class BssException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BssErrorCode errorCode;

    private String localizedMessage;

    public BssException(BssErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BssException(BssErrorCode errorCode, Throwable e) {
        super(e);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.name();
    }

    public HttpStatus getStatus() {
        return errorCode.status();
    }

    @Override
    public void printStackTrace() {
        if (errorCode.status().is5xxServerError()) {
            log.error("[{}]", errorCode, this);
        } else {
            log.warn("[{}]", errorCode, this);
        }
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
