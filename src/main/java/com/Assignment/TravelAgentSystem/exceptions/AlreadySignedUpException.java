package com.Assignment.TravelAgentSystem.exceptions;

public class AlreadySignedUpException extends RuntimeException{

    public AlreadySignedUpException(String message){
        super(message);
    }
}
