package com.ingenico.transferservice.service;

import com.ingenico.transferservice.persistence.entity.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Resource
    AccountRepository accountRepository;

    public com.ingenico.transferservice.model.Account addAccount(com.ingenico.transferservice.model.Account accountModel) {
        try {
            Account accountEntity = new Account(accountModel);
            accountRepository.save(accountEntity);
            accountModel.setId(accountEntity.getId());
            accountModel.setCreatedOn(accountEntity.getCreated_on());
            accountModel.setUpdatedOn(accountEntity.getUpdated_on());
        } catch (Exception e) {
            logger.error("Exception while saving account in AccountService.addAccount()");
            throw e;
        }
        return accountModel;
    }

}
