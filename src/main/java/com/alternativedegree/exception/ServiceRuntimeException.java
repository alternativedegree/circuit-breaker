package com.alternativedegree.exception;

public class ServiceRuntimeException extends RuntimeException {
    private final String message;

    public ServiceRuntimeException(Throwable throwable, String message) {
        super(throwable);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
