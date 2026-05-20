package com.dealer.drproj.exception;

public class EmptyUpdateRequestException extends RuntimeException{
    public EmptyUpdateRequestException(String message){
        super(message);
    }
}
