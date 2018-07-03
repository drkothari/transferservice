package com.ingenico.transferservice.service;

import com.ingenico.transferservice.dto.TransactionDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.entity.Transaction;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private AccountRepository accountRepository;

    public List<Account> transfer(TransactionDto transactionDto) {

        List<Account> accounts = new ArrayList<>();
        com.ingenico.transferservice.persistence.entity.Account sourceAccount = accountRepository.findByName(transactionDto.getSourceAccountName());
        com.ingenico.transferservice.persistence.entity.Account targetAccount = accountRepository.findByName(transactionDto.getTargetAccountName());

        synchronized (this) {
            sourceAccount.setBalance(sourceAccount.getBalance() - transactionDto.getAmount());
            sourceAccount.setUpdated_on(LocalDateTime.now());
            targetAccount.setBalance(targetAccount.getBalance() + transactionDto.getAmount());
            targetAccount.setUpdated_on(LocalDateTime.now());

            /*accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);*/

            saveTransfer(transactionDto, sourceAccount, targetAccount);

            accounts.add(enrichAccountModel(sourceAccount));
            accounts.add(enrichAccountModel(targetAccount));
        }
        return accounts;
    }

    private void saveTransfer(TransactionDto transactionDto, com.ingenico.transferservice.persistence.entity.Account sourceAccount, com.ingenico.transferservice.persistence.entity.Account targetAccount) {
        Transaction transactionEntity = new Transaction();
        transactionEntity.setSourceAccount(sourceAccount);
        transactionEntity.setTargetAccount(targetAccount);
        transactionEntity.setAmount(transactionDto.getAmount());
        transactionEntity.setCreated_on(LocalDateTime.now());
        transactionEntity.setUpdated_on(LocalDateTime.now());
        transactionRepository.save(transactionEntity);
    }

    private Account enrichAccountModel(com.ingenico.transferservice.persistence.entity.Account accountEntity) {
        Account account = new Account();
        account.setName(accountEntity.getName());
        account.setBalance(accountEntity.getBalance());
        account.setUpdatedOn(accountEntity.getUpdated_on());
        return account;
    }
}
