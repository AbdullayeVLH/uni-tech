package com.az.unitech.service;

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
import com.az.unitech.service.impl.UniTechServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UniTechServiceImplTest {

    private UniTechServiceImpl uniTechService;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private CurrencyRateService rateService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        rateService = mock(CurrencyRateService.class);
        uniTechService = new UniTechServiceImpl(accountRepository, userRepository, rateService) {
        };
    }

    @Test
    void testRegisterByPin_SuccessfulRegistration() throws AlreadyRegisteredPin {
        LoginRegisterRequest request = new LoginRegisterRequest("AZE123", "admin");
        when(userRepository.findUserByPin(request.getPin())).thenReturn(null);

        LoginRegisterResponse response = uniTechService.registerByPin(request);

        assertTrue(response.isSuccessful());
        verify(userRepository).save(any());
    }

    @Test
    void testRegisterByPin_AlreadyRegisteredPin() {
        LoginRegisterRequest request = new LoginRegisterRequest("AZE123", "admin");
        when(userRepository.findUserByPin(request.getPin())).thenReturn(new User());

        assertThrows(AlreadyRegisteredPin.class, () -> uniTechService.registerByPin(request));
    }

    @Test
    void testLoginByPin_SuccessfulLogin() throws WrongCredentials {
        LoginRegisterRequest request = new LoginRegisterRequest("AZE123", "admin");
        when(userRepository.findUserByPinAndPassword(request.getPin(), request.getPassword())).thenReturn(new User());

        LoginRegisterResponse response = uniTechService.loginByPin(request);

        assertTrue(response.isSuccessful());
    }

    @Test
    void testLoginByPin_WrongCredentials() {
        LoginRegisterRequest request = new LoginRegisterRequest("AZE123", "admin");
        when(userRepository.findUserByPinAndPassword(request.getPin(), request.getPassword())).thenReturn(null);

        assertThrows(WrongCredentials.class, () -> uniTechService.loginByPin(request));
    }

    @Test
    void testGetAccountsByPin() {
        String pin = "1234";
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.getAccountByPinAndState(pin, 'A')).thenReturn(accounts);

        GetAccount result = uniTechService.getAccountsByPin(pin);

        assertEquals(accounts, result.getAccounts());
    }

    @Test
    void testTransferMoney_SuccessfulTransfer() throws DeactiveAccountTransfer, NotEnoughMoney, AccountNotExist, SameAccountTransfer {
        Transfer request = new Transfer("1234", "5678", "AZE123", 100.0);
        Account sender = new Account();
        sender.setBalance(200.0);
        Account receiver = new Account();
        when(accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A')).thenReturn(sender);
        when(accountRepository.getAccountByAccountNumber(request.getReceiverAccountNumber())).thenReturn(receiver);

        boolean result = uniTechService.transferMoney(request);

        assertTrue(result);
        assertEquals(100.0, sender.getBalance());
        assertEquals(100.0, receiver.getBalance());
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void testTransferMoney_NotEnoughMoney() {
        Transfer request = new Transfer("1234", "5678", "AZE123", 200.0); // Amount exceeds sender's balance
        Account sender = new Account();
        sender.setBalance(100.0);
        when(accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A')).thenReturn(sender);

        assertThrows(NotEnoughMoney.class, () -> uniTechService.transferMoney(request));
    }

    @Test
    void testTransferMoney_SameAccountTransfer() {
        Transfer request = new Transfer("1234", "5678", "AZE123", 100.0); // Same account number
        Account sender = new Account();
        when(accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A')).thenReturn(sender);

        assertThrows(SameAccountTransfer.class, () -> uniTechService.transferMoney(request));
    }

    @Test
    void testTransferMoney_AccountNotExist() {
        Transfer request = new Transfer("1234", "5678", "AZE123", 100.0);
        Account sender = new Account();
        when(accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A')).thenReturn(sender);
        when(accountRepository.getAccountByAccountNumber(request.getReceiverAccountNumber())).thenReturn(null);

        assertThrows(AccountNotExist.class, () -> uniTechService.transferMoney(request));
    }

    @Test
    void testTransferMoney_DeactiveAccountTransfer() {
        Transfer request = new Transfer("1234", "5678", "AZE123", 100.0);
        Account sender = new Account();
        sender.setState('A');
        Account receiver = new Account();
        receiver.setState('S'); // Deactivated account
        when(accountRepository.getAccountByPinAndAccountNumberAndState(request.getPin(), request.getSenderAccountNumber(), 'A')).thenReturn(sender);
        when(accountRepository.getAccountByAccountNumber(request.getReceiverAccountNumber())).thenReturn(receiver);

        assertThrows(DeactiveAccountTransfer.class, () -> uniTechService.transferMoney(request));
    }

    @Test
    void testCurrencyRates() {
        String currency = "aze-usd";
        double rate = 1.0;
        when(rateService.getCurrencyRate(currency)).thenReturn(rate);

        double result = uniTechService.currencyRates(currency);

        assertEquals(rate, result);
    }
}
