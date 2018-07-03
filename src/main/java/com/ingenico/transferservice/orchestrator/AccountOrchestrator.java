package com.ingenico.transferservice.orchestrator;

import com.ingenico.transferservice.dto.AccountDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.service.AccountService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AccountOrchestrator {

    @Resource
    private AccountService accountService;

    public Account addAccount(AccountDto accountDto){
        return accountService.addAccount(createModel(accountDto));
    }

    private Account createModel(AccountDto accountDto){
        Account accountModel = new Account();
        accountModel.setName(accountDto.getName());
        accountModel.setBalance(accountDto.getBalance());
        return accountModel;
    }

}
