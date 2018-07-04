package com.ingenico.transferservice.service;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.persistence.entity.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AccountService responsible to call persistence layer for Account addition
 */
@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Resource
    AccountRepository accountRepository;

    /**
     * This method will add the account in database
     * @param account model
     * @return added new Account resource
     * @throws TransferServiceException
     */
    public com.ingenico.transferservice.model.Account addAccount(com.ingenico.transferservice.model.Account accountModel) throws TransferServiceException {
        try {
            Account accountEntity = new Account(accountModel);
            accountRepository.save(accountEntity);
            accountModel.setCreatedOn(accountEntity.getCreated_on());
            accountModel.setUpdatedOn(accountEntity.getUpdated_on());
        } catch (Exception e) {
            logger.error("Exception while saving account in AccountService.addAccount()");
            TransferServiceException transferServiceException = new TransferServiceException();
            transferServiceException.setMessage("TECHNICAL_ERROR");
            throw transferServiceException;
        }
        return accountModel;
    }

}
