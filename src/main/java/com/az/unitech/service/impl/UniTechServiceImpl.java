package com.az.unitech.service.impl;

import com.az.unitech.client.CurrencyRateService;
import com.az.unitech.error.*;
import com.az.unitech.model.entity.Account;
import com.az.unitech.model.entity.User;
import com.az.unitech.model.request.LoginRegisterRequest;
import com.az.unitech.model.request.Transfer;
import com.az.unitech.model.response.GetAccount;
import com.az.unitech.model.response.LoginRegisterResponse;
import com.az.unitech.repository.AccountRepository;
import com.az.unitech.repository.UserRepository;
import com.az.unitech.service.UniTechService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UniTechServiceImpl implements UniTechService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CurrencyRateService rateService;

    public UniTechServiceImpl(AccountRepository accountRepository, UserRepository userRepository, CurrencyRateService rateService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.rateService = rateService;
    }

    @Override
    public LoginRegisterResponse registerByPin(LoginRegisterRequest request) throws AlreadyRegisteredPin {
        User user = userRepository.findUserByPin(request.getPin());
        if (Objects.isNull(user)){
            userRepository.save(User.builder().pin(request.getPin()).password(request.getPassword()).build());
            return LoginRegisterResponse.builder().successful(true).build();
        }
        throw new AlreadyRegisteredPin("Pin :" + request.getPin() + " have already registered.");
    }

    @Override
    public LoginRegisterResponse loginByPin(LoginRegisterRequest request) throws WrongCredentials {
        User user = userRepository.findUserByPinAndPassword(request.getPin(), request.getPassword());
        if (Objects.isNull(user)){
            throw new WrongCredentials("Wrong pin or password.");
        }
        return LoginRegisterResponse.builder().successful(true).build();
    }

    @Override
    public GetAccount getAccountsByPin(String pin) {
        List<Account> accounts = accountRepository.getAccountByPinAndState(pin, 'A');
        return GetAccount.builder().accounts(accounts).build();
    }

    @Override
    public Boolean transferMoney(Transfer request) throws NotEnoughMoney, SameAccountTransfer, DeactiveAccountTransfer, AccountNotExist {
        Account sender = accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A');
        Account receiver = accountRepository.getAccountByAccountNumber(request.getReceiverAccountNumber());
        if (sender.getBalance()< request.getAmount()){
            throw new NotEnoughMoney("Your balance : " + sender.getBalance() + " is less then transfer amount : " + request.getAmount());
        } else if (sender.getAccountNumber().equals(request.getReceiverAccountNumber())){
            throw new SameAccountTransfer("Receiver account number : " + request.getReceiverAccountNumber() + " is your account number.");
        } else if (Objects.isNull(receiver)){
            throw new AccountNotExist("Account : " + request.getReceiverAccountNumber() + " does not exist.");
        } else if (receiver.getState() == 'S'){
            throw new DeactiveAccountTransfer("The account you want to transfer : " + receiver.getAccountNumber() + " money is not active.");
        }
        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());
        accountRepository.save(sender);
        accountRepository.save(receiver);
        return true;
    }

    @Override
    public Double currencyRates(String currency) {
        return rateService.getCurrencyRate(currency);
    }
}
