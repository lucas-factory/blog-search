package com.lucas.platform.searchcore.exception;

import org.springframework.http.HttpStatus;

public enum BssErrorCode {
    INTERNAL_SERVER_ERROR(10000, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(10001, HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_PUBLISHER(10002, HttpStatus.BAD_REQUEST),
    SAVE_KEYWORD_FAIL(10003, HttpStatus.INTERNAL_SERVER_ERROR),
    SearchClientResponseError(10004, HttpStatus.INTERNAL_SERVER_ERROR),
    SearchClientRequestError(10005, HttpStatus.INTERNAL_SERVER_ERROR),
    NotDefinedSortType(10006, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;

    private final HttpStatus status;

    BssErrorCode(int code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public int code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

}
