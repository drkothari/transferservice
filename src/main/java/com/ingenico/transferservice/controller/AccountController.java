package com.ingenico.transferservice.controller;

import com.ingenico.transferservice.dto.AccountDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.orchestrator.AccountOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(produces = {"application/hal+json"})
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AccountOrchestrator accountOrchestrator;

    @PostMapping("/accounts")
    public ResponseEntity<Account> addAccount(@RequestBody AccountDto accountDto){
        logger.info("Adding Account with name ", accountDto.getName());
        final Account account = accountOrchestrator.addAccount(accountDto);
        
        //Using Model class as resource class to return the response as there is no change
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
