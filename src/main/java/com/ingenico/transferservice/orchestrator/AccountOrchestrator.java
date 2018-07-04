package com.ingenico.transferservice.orchestrator;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.service.AccountService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AccountOrchestrator responsible to validate inputs and call required services for Accounts
 */
@Component
public class AccountOrchestrator {

    @Resource
    private AccountService accountService;

    /**
     * 
     * @param account model data sent in request
     * @return Account model with created/ updated dates
     * @throws TransferServiceException
     */
    public Account addAccount(Account account) throws TransferServiceException{
        return accountService.addAccount(account);
    }
}
