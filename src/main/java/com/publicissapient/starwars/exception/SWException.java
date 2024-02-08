package com.publicissapient.starwars.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SWException extends Exception {
    private String error;
    public SWException(String error){
        this.error = error;
    }
}
