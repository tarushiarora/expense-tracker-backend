package com.example.expense_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// this annotation tells spring to look for all controllers
// if any controller throws an error, it will handle
@ControllerAdvice
public class GlobalExceptionHandler {
    //1.) to handle runtime exception
    // like user not found or transaction not found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex){
        //custom error object
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        // returning with 404 status code
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //2.) generic exception handling
    // like db down, etc..
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage()
        );

        // return with 500 status code
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
