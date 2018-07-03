package com.ingenico.transferservice.orchestrator;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.dto.TransferDto;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransferRepository;
import com.ingenico.transferservice.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TransferOrchestrator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TransferRepository transferRepository;

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private TransferService transferService;

    public List<Account> transfer(TransferDto transferDto) throws TransferServiceException {
        logger.info("inside TransferOrchestrator.transfer");
        validateTransfer(transferDto);
        return transferService.transfer(transferDto);
    }

    private void validateTransfer(TransferDto transferDto) throws TransferServiceException{
        //validate both accounts, overwithdrawn

        TransferServiceException transferServiceException = new TransferServiceException();

        //validating TargetAccount existence

        if (! "".equals(transferDto.getTargetAccountName())) {
            final com.ingenico.transferservice.persistence.entity.Account targetAccount = accountRepository.findByName(transferDto.getTargetAccountName());
            if ( targetAccount == null) {
                transferServiceException.setMessage("TARGET_ACCOUNT_NOT_FOUND");
                throw transferServiceException;
            }
        } else {
            transferServiceException.setMessage("TARGET_ACCOUNT_NAME_IS_EMPTY");
            throw transferServiceException;
        }

        //validating SourceAccount existence
        if (! "".equals(transferDto.getSourceAccountName())) {
            final com.ingenico.transferservice.persistence.entity.Account sourceAccount = accountRepository.findByName(transferDto.getSourceAccountName());
            if (sourceAccount == null) {
                transferServiceException.setMessage("SOURCE_ACCOUNT_NOT_FOUND");
                throw transferServiceException;
            } else {
                //validating over transfer
                double difference = sourceAccount.getBalance() - transferDto.getAmount();
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
