package com.ingenico.transferservice.controller;

import com.ingenico.transferservice.context.TransferServiceException;
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

/**
 * AccountController To add account resource
 */
@RestController
@RequestMapping(produces = {"application/hal+json"})
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AccountOrchestrator accountOrchestrator;

    /**
     *
     * @param accountModel request body
     * @return added rnew Account resource
     * @throws TransferServiceException
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> addAccount(@RequestBody Account accountModel) throws TransferServiceException{
        logger.info("Adding Account with name {}", accountModel.getName());
        accountOrchestrator.addAccount(accountModel);
        
        //Using Model class as resource class to return the response as there is no change
        return new ResponseEntity<>(accountModel, HttpStatus.CREATED);
    }
}
