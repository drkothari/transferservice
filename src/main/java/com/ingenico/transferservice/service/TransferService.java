package com.ingenico.transferservice.service;

import com.ingenico.transferservice.dto.TransferDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.entity.Transfer;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransferRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    @Resource
    private TransferRepository transferRepository;

    @Resource
    private AccountRepository accountRepository;

    public List<Account> transfer(TransferDto transferDto) {

        List<Account> accounts = new ArrayList<>();
        com.ingenico.transferservice.persistence.entity.Account sourceAccount = accountRepository.findByName(transferDto.getSourceAccountName());
        com.ingenico.transferservice.persistence.entity.Account targetAccount = accountRepository.findByName(transferDto.getTargetAccountName());

        synchronized (this) {
            sourceAccount.setBalance(sourceAccount.getBalance() - transferDto.getAmount());
            sourceAccount.setUpdated_on(LocalDateTime.now());
            targetAccount.setBalance(targetAccount.getBalance() + transferDto.getAmount());
            targetAccount.setUpdated_on(LocalDateTime.now());

            /*accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);*/

            saveTransfer(transferDto, sourceAccount, targetAccount);

            accounts.add(enrichAccountModel(sourceAccount));
            accounts.add(enrichAccountModel(targetAccount));
        }
        return accounts;
    }

    private void saveTransfer(TransferDto transferDto, com.ingenico.transferservice.persistence.entity.Account sourceAccount, com.ingenico.transferservice.persistence.entity.Account targetAccount) {
        Transfer transferEntity = new Transfer();
        transferEntity.setSourceAccount(sourceAccount);
        transferEntity.setTargetAccount(targetAccount);
        transferEntity.setAmount(transferDto.getAmount());
        transferEntity.setCreated_on(LocalDateTime.now());
        transferEntity.setUpdated_on(LocalDateTime.now());
        transferRepository.save(transferEntity);
    }

    private Account enrichAccountModel(com.ingenico.transferservice.persistence.entity.Account accountEntity) {
        Account account = new Account();
        account.setName(accountEntity.getName());
        account.setBalance(accountEntity.getBalance());
        account.setUpdatedOn(accountEntity.getUpdated_on());
        return account;
    }
}
