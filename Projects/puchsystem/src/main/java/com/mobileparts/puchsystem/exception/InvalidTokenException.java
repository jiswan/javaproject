package com.mobileparts.puchsystem.exception;

public class InvalidTokenException extends RuntimeException{

    public  InvalidTokenException(String message)
    {
        super(message);
    }

    public InvalidTokenException()
    {
        super("Invalid or expired token. This link may have already been used.");
    }

}
