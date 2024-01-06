package com.Assignment.TravelAgentSystem.Exceptions;

public class AlreadySignedUpException extends RuntimeException{

    public AlreadySignedUpException(String message){
        super(message);
    }
}
