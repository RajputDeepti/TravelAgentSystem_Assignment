package com.Assignment.TravelAgentSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdAlreadyExistException extends RuntimeException {

    public IdAlreadyExistException(String message){
        super(message);
    }
}
