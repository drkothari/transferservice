package com.ingenico.transferservice.controller;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.dto.TransferDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.orchestrator.TransferOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TransferController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransferOrchestrator transferOrchestrator;

    @PostMapping("/transfers")
    public ResponseEntity<List<Account>> transferFund(@RequestBody TransferDto transferDto) throws TransferServiceException {
        logger.info("inside transferFund");
        final List<Account> accounts = transferOrchestrator.transfer(transferDto);
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.ACCEPTED);
    }
}
