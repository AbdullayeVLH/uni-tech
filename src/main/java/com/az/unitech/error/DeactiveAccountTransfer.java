package com.az.unitech.error;

public class DeactiveAccountTransfer extends Exception{
    public DeactiveAccountTransfer (String errorMessage){
        super(errorMessage);
    }
}
