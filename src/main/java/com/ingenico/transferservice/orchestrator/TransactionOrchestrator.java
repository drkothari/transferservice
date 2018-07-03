package com.ingenico.transferservice.orchestrator;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.model.Transaction;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransactionRepository;
import com.ingenico.transferservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TransactionOrchestrator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private TransactionService transactionService;

    public List<Account> transfer(Transaction transaction) throws TransferServiceException {
        logger.info("inside TransferOrchestrator.transfer");
        validateTransfer(transaction);
        return transactionService.transfer(transaction);
    }

    private void validateTransfer(Transaction transaction) throws TransferServiceException{
        //validate both accounts, overwithdrawn

        TransferServiceException transferServiceException = new TransferServiceException();

        //validating TargetAccount existence

        if (! "".equals(transaction.getTargetAccount().getName())) {
            final com.ingenico.transferservice.persistence.entity.Account targetAccount = accountRepository.findByName(transaction.getTargetAccount().getName());
            if ( targetAccount == null) {
                transferServiceException.setMessage("TARGET_ACCOUNT_NOT_FOUND");
                throw transferServiceException;
            }
        } else {
            transferServiceException.setMessage("TARGET_ACCOUNT_NAME_IS_EMPTY");
            throw transferServiceException;
        }

        //validating SourceAccount existence
        if (! "".equals(transaction.getSourceAccount().getName())) {
            final com.ingenico.transferservice.persistence.entity.Account sourceAccount = accountRepository.findByName(transaction.getSourceAccount().getName());
            if (sourceAccount == null) {
                transferServiceException.setMessage("SOURCE_ACCOUNT_NOT_FOUND");
                throw transferServiceException;
            } else {
                //validating over transfer
                double difference = sourceAccount.getBalance() - transaction.getAmount();
                if (difference < 0) {
                    transferServiceException.setMessage("INSUFFICIENT_BALANCE_IN_SOURCE_ACCOUNT");
                    throw transferServiceException;
                }
            }
        } else {
            transferServiceException.setMessage("SOURCE_ACCOUNT_NAME_IS_EMPTY");
            throw transferServiceException;
        }


    }
}
