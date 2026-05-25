package com.dealer.drproj.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPassword(InvalidPasswordException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            Map.of(
                "error","Invalid password.",
                "message",ex.getMessage(),
                "status",401
            )
        );
    }

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

    @ExceptionHandler(EmptyUpdateRequestException.class)
    public ResponseEntity<?> handleEmptyUpdate(EmptyUpdateRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "error","Empty Update request body.",
                "message",ex.getMessage(),
                "status",400
            )
        );
    }

    @ExceptionHandler(InvalidMsisdnException.class)
    public ResponseEntity<?> handleInvalidMsisdn(InvalidMsisdnException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "error","Invalid MSISDN",
                "message",ex.getMessage(),
                "status",400
            )
        );
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<?> handleInvalidStatus(InvalidStatusException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "error","Invalid Status",
                "message",ex.getMessage(),
                "status",400
            )
        );
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<?> handleInvalidType(InvalidTypeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of(
                "error","Invalid Type",
                "message",ex.getMessage(),
                "status",400
            )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationError(MethodArgumentNotValidException ex){
        Map<String,Object> errorDetails=new HashMap<>();
        String errorMessage=ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        errorDetails.put("status",HttpStatus.BAD_REQUEST.value());
        errorDetails.put("message",errorMessage);
        errorDetails.put("error","Validation Error");
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
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
