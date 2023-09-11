package com.az.unitech.repository;

import com.az.unitech.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> getAccountByPinAndState(String pin, Character state);

    Account getAccountByPinAndAccountNumberAndState(String pin, String accountNumber, Character state);

    Account getAccountByAccountNumber(String accountNumber);
}
