package de.vfh.workhourstracker.shared.util;

import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private T data; // T steht für den generischen Typ
    private List<ErrorResponse> errors;

    public ApiResponse(boolean success, T data, List<ErrorResponse> errors) {
        this.success = success;
        this.data = data;
        this.errors = errors;
    }
}
