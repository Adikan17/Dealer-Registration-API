package com.dealer.drproj.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DealerNotFoundException.class)
    public ResponseEntity<?> handleDealerNotFound(DealerNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of(
                "error","Dealer Not Found",
                "message",ex.getMessage(),
                "status",404
            )
        );
    }

    @ExceptionHandler(DuplicateDealerException.class)
    public ResponseEntity<?> handleDuplicateDealer(DuplicateDealerException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of(
                "error","Duplicate MSISDN",
                "message",ex.getMessage(),
                "status",409
            )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "error","Validation Error",
                "message","Invalid or Missing feilds",
                "status",400
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            Map.of(
                "error","Internal Server Error",
                "message",ex.getMessage(),
                "status",500
            )
        );
    }
}
