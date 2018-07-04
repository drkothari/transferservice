package com.ingenico.transferservice.service;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.model.Transaction;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TransactionService responsible to call persistence layer for amount transfer
 */
@Service
public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private AccountRepository accountRepository;

    /**
     * this method will actually transfer the money between accounts and returns source and target modified account
     * @param transaction model
     * @return list of accounts which are requested for transfer, with updated balance
     * @throws TransferServiceException
     */
    public List<Account> transfer(Transaction transaction) throws TransferServiceException{
        TransferServiceException transferServiceException = new TransferServiceException();
        List<Account> accounts = new ArrayList<>();

        try {
            com.ingenico.transferservice.persistence.entity.Account sourceAccount = accountRepository.findByName(transaction.getSourceAccount().getName());
            com.ingenico.transferservice.persistence.entity.Account targetAccount = accountRepository.findByName(transaction.getTargetAccount().getName());

            synchronized (this) {
                sourceAccount.setBalance(sourceAccount.getBalance() - transaction.getAmount());
                sourceAccount.setUpdated_on(LocalDateTime.now());
                targetAccount.setBalance(targetAccount.getBalance() + transaction.getAmount());
                targetAccount.setUpdated_on(LocalDateTime.now());

                com.ingenico.transferservice.persistence.entity.Transaction transactionEntity = new com.ingenico.transferservice.persistence.entity.Transaction(transaction, sourceAccount, targetAccount);
                transactionRepository.save(transactionEntity);
            }

            accounts.add(enrichAccountModel(sourceAccount));
            accounts.add(enrichAccountModel(targetAccount));

        } catch (Exception e) {
            logger.error("Exception while saving account in TransactionService.transfer()");
            transferServiceException.setMessage("TECHNICAL_ERROR");
        }
        return accounts;
    }


    private Account enrichAccountModel(com.ingenico.transferservice.persistence.entity.Account accountEntity) {
        Account account = new Account();
        account.setName(accountEntity.getName());
        account.setBalance(accountEntity.getBalance());
        account.setUpdatedOn(accountEntity.getUpdated_on());
        return account;
    }
}
