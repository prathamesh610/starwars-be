package com.publicissapient.starwars.exception;

import com.publicissapient.starwars.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class ApplicationExceptionHandler {

    @Getter
    public static class WrappedException extends RuntimeException {
        private final Exception exception;
        public WrappedException(Exception exception) {
            super(exception);
            this.exception = exception;
        }
    }
    public static class ApplicationWrappedException extends RuntimeException {
        private final SWException exception;
        public ApplicationWrappedException(SWException exception) {
            super(exception);
            this.exception = exception;
        }
        public Exception getException() {
            return exception;
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationWrappedException.class)
    public ResponseDTO handleException(ApplicationWrappedException e){
        return new ResponseDTO(e.exception.getError());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(WrappedException.class)
    public ResponseDTO handleException(WrappedException e){
        return new ResponseDTO(e.exception.getMessage());
    }
}
