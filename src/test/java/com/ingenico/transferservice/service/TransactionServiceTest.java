package com.ingenico.transferservice.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.model.Transaction;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.persistence.repository.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
	
	@InjectMocks
	private TransactionService transactionService;	
	@Mock
    private TransactionRepository transactionRepository;	
	@Mock
    private AccountRepository accountRepository;
	
	@Test
	public void testTransfer() throws Exception {
		// create a model transaction to save
		Transaction transaction = createTransaction("Garima", "Gudiya", 100.45);
		// Mock account repository to get 2 persisted accounts
		Mockito.when(accountRepository.findByName("Garima")).thenReturn(createPersistedAccount("Garima", 1000.45));	
		Mockito.when(accountRepository.findByName("Gudiya")).thenReturn(createPersistedAccount("Gudiya", 100.05));
		// Mock transaction repository to save and return persisted transaction entity
		Mockito.when(transactionRepository.save(any())).thenReturn(createPersistedTransaction("Garima", "Gudiya", 100.45));

		// test method call
		List<Account> accounts = transactionService.transfer(transaction);
		
		// assert check the data if modified correctly
		assertEquals(2, accounts.size());
		assertEquals("Garima", accounts.get(0).getName());
		assertEquals(900.00, accounts.get(0).getBalance(), 0);
		assertEquals("Gudiya", accounts.get(1).getName());
		assertEquals(200.50, accounts.get(1).getBalance(), 0);		
	}
	
	private Transaction createTransaction(String sourceAccountName, String targetAccountName, double transferAmount) {
		Transaction transaction = new Transaction();
		Account sourceAccount = new Account();
		sourceAccount.setName(sourceAccountName);
		transaction.setSourceAccount(sourceAccount);
		Account targetAccount = new Account();
		targetAccount.setName(targetAccountName);
		transaction.setTargetAccount(targetAccount);
		transaction.setAmount(transferAmount);
		return transaction;
	}
	
	private com.ingenico.transferservice.persistence.entity.Transaction createPersistedTransaction(String sourceAccountName, String targetAccountName, double transferAmount) {
		com.ingenico.transferservice.persistence.entity.Transaction persistedTransaction = new com.ingenico.transferservice.persistence.entity.Transaction();
		com.ingenico.transferservice.persistence.entity.Account sourceAccount = new com.ingenico.transferservice.persistence.entity.Account();
		sourceAccount.setName(sourceAccountName);
		persistedTransaction.setSourceAccount(sourceAccount);
		com.ingenico.transferservice.persistence.entity.Account targetAccount = new com.ingenico.transferservice.persistence.entity.Account();
		targetAccount.setName(targetAccountName);
		persistedTransaction.setTargetAccount(targetAccount);
		persistedTransaction.setAmount(transferAmount);
		return persistedTransaction;
	}
	

	private com.ingenico.transferservice.persistence.entity.Account createPersistedAccount(String accountName, double amount) {
		com.ingenico.transferservice.persistence.entity.Account account = new com.ingenico.transferservice.persistence.entity.Account();
		account.setName(accountName);
		account.setBalance(amount);
		return account;
	}
	
}
