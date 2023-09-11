package com.az.unitech.service;

import com.az.unitech.error.*;
import com.az.unitech.model.request.Transfer;
import com.az.unitech.model.response.GetAccount;
import com.az.unitech.model.response.LoginRegisterResponse;
import com.az.unitech.model.request.LoginRegisterRequest;

public interface UniTechService {
    LoginRegisterResponse registerByPin(LoginRegisterRequest request) throws AlreadyRegisteredPin;

    LoginRegisterResponse loginByPin(LoginRegisterRequest request) throws WrongCredentials;

    GetAccount getAccountsByPin(String pin);

    Boolean transferMoney(Transfer request) throws NotEnoughMoney, SameAccountTransfer, DeactiveAccountTransfer, AccountNotExist;

    Double currencyRates(String currency);
}
