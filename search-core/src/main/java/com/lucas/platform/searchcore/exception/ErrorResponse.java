package com.lucas.platform.searchcore.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int product;
    private int module;
    private int error;
    private int line;
    private String text;
    private String errorCode;
    private String exceptionClassName;
    private List<FieldMessageError> fieldErrors;

    private ErrorResponse(Builder builder) {
        this.product = builder.product;
        this.module = builder.module;
        this.line = builder.line;
        this.error = builder.error;
        this.text = builder.text;
        this.errorCode = builder.errorCode;
        this.exceptionClassName = builder.exceptionClassName;
        this.fieldErrors = builder.fieldErrors;
    }

    public static Builder builder(int product, int module, Throwable t) {
        return new Builder(product, module, t.getStackTrace()[0].getLineNumber(), t.getClass().getSimpleName());
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldMessageError {

        private String field;
        private String object;
        private String reason;

    }

    public static class Builder {

        private final int product;
        private final int module;
        private final int line;
        private final String exceptionClassName;
        private int error;
        private String text;
        private String errorCode;
        private List<FieldMessageError> fieldErrors;

        public Builder(int product, int module, int line, String exceptionClassName) {
            this.product = product;
            this.module = module;
            this.line = line;
            this.exceptionClassName = exceptionClassName;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            try {
                this.error = BssErrorCode.valueOf(errorCode).code();
            } catch (IllegalArgumentException e) {
                this.error = BssErrorCode.INTERNAL_SERVER_ERROR.code();
            }
            return this;
        }

        public Builder fieldErrors(List<FieldMessageError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }

    }

}
