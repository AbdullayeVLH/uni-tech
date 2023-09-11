package com.az.unitech.error;

public class AccountNotExist extends Exception{

    public AccountNotExist (String errorMessage){
        super(errorMessage);
    }
}
