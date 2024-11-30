package de.vfh.workhourstracker.shared.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("field")
    private String field;

    @JsonProperty("errorCode")
    private String errorCode;

    public ErrorResponse(String message, String field, String errorCode) {
        this.message = message;
        this.field = field;
        this.errorCode = errorCode;
    }
}
