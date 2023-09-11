package com.az.unitech.error;

public class WrongCredentials extends Exception{
    public WrongCredentials (String errorMessage){
        super(errorMessage);
    }
}
