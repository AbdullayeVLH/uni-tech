package com.az.unitech.error;

public class AlreadyRegisteredPin extends Exception{
    public AlreadyRegisteredPin (String errorMessage){
        super(errorMessage);
    }
}
