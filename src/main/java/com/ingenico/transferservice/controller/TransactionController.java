package com.ingenico.transferservice.controller;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.model.Transaction;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.orchestrator.TransactionOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * TransactionController handle the given transfer of amount between accounts
 */
@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransactionOrchestrator transactionOrchestrator;

    /**
     * This method calls orchestrator and saves transaction
     * @param transaction request body
     * @return list of account which are requested for transfer, with updated balance
     * @throws TransferServiceException
     */
    @PostMapping("/transfers")
    public ResponseEntity<List<Account>> transferFund(@RequestBody Transaction transaction) throws TransferServiceException {
        logger.info("inside transferFund");
        final List<Account> accounts = transactionOrchestrator.transfer(transaction);
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.ACCEPTED);
    }
}
