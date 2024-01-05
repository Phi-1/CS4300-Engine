package dev.stormwatch.cs4300.util;

import java.util.NoSuchElementException;

public class Result {

    private static final Result SUCCESS = new Result();

    private String errorMessage;

    private Result() {}

    private Result(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static Result success() {
        return SUCCESS;
    }

    public static Result error(String message) {
        return new Result(message);
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public boolean isError() { return this != SUCCESS; }

    public String getErrorMessage() {
        if (this.isSuccess()) {
            throw new NoSuchElementException("Attempted to access error message on successful Result");
        }
        return this.errorMessage;
    }

}
