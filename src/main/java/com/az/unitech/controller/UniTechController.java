package com.az.unitech.controller;

import com.az.unitech.error.*;
import com.az.unitech.model.request.Transfer;
import com.az.unitech.model.response.GetAccount;
import com.az.unitech.model.response.LoginRegisterResponse;
import com.az.unitech.model.request.LoginRegisterRequest;
import com.az.unitech.service.UniTechService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1")
public class UniTechController {

    private final UniTechService uniTechService;

    public UniTechController(UniTechService uniTechService) {
        this.uniTechService = uniTechService;
    }

    @PostMapping("/registerByPin")
    public ResponseEntity<LoginRegisterResponse> registerByPin (@RequestBody @Validated LoginRegisterRequest request) throws AlreadyRegisteredPin {
        LoginRegisterResponse response = uniTechService.registerByPin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/loginByPin")
    public ResponseEntity<LoginRegisterResponse> loginByPin (@RequestBody @Validated LoginRegisterRequest request) throws WrongCredentials {
        LoginRegisterResponse response = uniTechService.loginByPin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAccounts/{pin}")
    public GetAccount getAccountsByPin(@PathVariable String pin) {
        return uniTechService.getAccountsByPin(pin);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Boolean> transferMoney (@RequestBody @Validated Transfer request)
            throws DeactiveAccountTransfer, NotEnoughMoney, AccountNotExist, SameAccountTransfer {
        Boolean response = uniTechService.transferMoney(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currencyRates/{currency}")
    public Double currencyRates(@PathVariable String currency) {
        return uniTechService.currencyRates(currency);
    }
}
