package com.publicissapient.starwars.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    INVALID_NAME("The name entered is invalid!"),
    STATUS_SAME("The status value entered is already configured!"),
    SW_API_RESPONSE_NULL("Error! Failed to get data from SW-api!"),
    INVALID_TYPE("The type entered is invalid!"),
    ;
    private String errorMessage;

    ErrorCodes(String errorMessage){
        this.errorMessage = errorMessage;
    }

}
