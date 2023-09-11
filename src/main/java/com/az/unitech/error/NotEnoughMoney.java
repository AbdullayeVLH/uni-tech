package com.az.unitech.error;

public class NotEnoughMoney extends Exception{
    public NotEnoughMoney (String errorMessage){
        super(errorMessage);
    }
}
