package com.az.unitech.error;

public class SameAccountTransfer extends Exception{
    public SameAccountTransfer (String errorMessage){
        super(errorMessage);
    }
}
