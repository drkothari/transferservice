package com.ingenico.transferservice.orchestrator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ingenico.transferservice.context.TransferServiceException;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.model.Transaction;
import com.ingenico.transferservice.persistence.repository.AccountRepository;
import com.ingenico.transferservice.service.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class TransactionOrchestratorTest {

	@InjectMocks
	private TransactionOrchestrator transactionOrchestrator;	
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private TransactionService transactionService;
	
	/**
	 * test success transfer
	 */
	@Test
	public void testTransferSuccessCase() throws Exception {
		Transaction transaction = createTransaction("Garima", "Gudiya", 100.45);
		// mock the source and target accounts
		when(accountRepository.findByName("Garima")).thenReturn(getPersistedAccount("Garima", 1000.45));
		when(accountRepository.findByName("Gudiya")).thenReturn(getPersistedAccount("Gudiya", 100.45));
		// mock service to transfer the amount
		when(transactionService.transfer(transaction)).thenReturn(getAccounts("Garima", 900.00, "Gudiya", 200.90));
		
		List<Account> accounts = transactionOrchestrator.transfer(transaction);
		
		// assert check on data
		assertEquals(2, accounts.size());
		assertEquals("Garima", accounts.get(0).getName());
		assertEquals(900.00, accounts.get(0).getBalance(), 0);
		assertEquals("Gudiya", accounts.get(1).getName());
		assertEquals(200.90, accounts.get(1).getBalance(), 0);	
		
	}
	
	/**
	 * test when target account name not available
	 */
	@Test
	public void testTransferValidationFailureTargetNameNotAvailable() throws Exception {
		Transaction transaction = createTransaction("Garima1", "", 100.45);
		try {
			transactionOrchestrator.transfer(transaction);			
		}
		catch (TransferServiceException e) {
			assertEquals("TARGET_ACCOUNT_NAME_IS_EMPTY", e.getMessage());
			// verifies that exception was thrown and service was not invoked
			verify(accountRepository, never()).findByName(any());
			verify(transactionService, never()).transfer(transaction);
		}
	}
	
	/**
	 * test when target account not found
	 */
	@Test
	public void testTransferValidationFailureTargetNotFound() throws Exception {
		Transaction transaction = createTransaction("Garima1", "Gudiya1", 100.45);
		//mock the target to null
		when(accountRepository.findByName("Gudiya1")).thenReturn(null);

		try {
			transactionOrchestrator.transfer(transaction);			
		}
		catch (TransferServiceException e) {
			assertEquals("TARGET_ACCOUNT_NOT_FOUND", e.getMessage());
			// verifies that exception was thrown and service was not invoked
			verify(transactionService, never()).transfer(transaction);
		}
	}
	
	/**
	 * test when source account name not available
	 */
	@Test
	public void testTransferValidationFailureSourceNameNotAvailable() throws Exception {
		Transaction transaction = createTransaction("", "Gudiya", 100.45);
		when(accountRepository.findByName("Gudiya")).thenReturn(getPersistedAccount("Gudiya", 100.45));
		try {
			transactionOrchestrator.transfer(transaction);			
		}
		catch (TransferServiceException e) {
			assertEquals("SOURCE_ACCOUNT_NAME_IS_EMPTY", e.getMessage());
			// verifies that exception was thrown and service was not invoked
			verify(transactionService, never()).transfer(transaction);
		}
	}
	
	/**
	 * test when source account not found
	 */
	@Test
	public void testTransferValidationFailureSourceNotFound() throws Exception {
		Transaction transaction = createTransaction("Garima1", "Gudiya1", 100.45);
		// mock the source accounts
		when(accountRepository.findByName("Garima1")).thenReturn(null);
		//mock the target to null
		when(accountRepository.findByName("Gudiya1")).thenReturn(getPersistedAccount("Garima1", 100.00));

		try {
			transactionOrchestrator.transfer(transaction);			
		}
		catch (TransferServiceException e) {
			assertEquals("SOURCE_ACCOUNT_NOT_FOUND", e.getMessage());
			// verifies that exception was thrown and service was not invoked
			verify(transactionService, never()).transfer(transaction);
		}
	}
	
	/**
	 * test insufficient balance in source account
	 */
	@Test
	public void testTransferValidationFailureInsufficientBalance() throws Exception {
		Transaction transaction = createTransaction("Garima3", "Gudiya3", 100.45);
		// mock the source and target accounts
		when(accountRepository.findByName("Garima3")).thenReturn(getPersistedAccount("Garima3", 10.45));
		when(accountRepository.findByName("Gudiya3")).thenReturn(getPersistedAccount("Gudiya3", 100.45));

		try {
			transactionOrchestrator.transfer(transaction);			
		}
		catch (TransferServiceException e) {
			assertEquals("INSUFFICIENT_BALANCE_IN_SOURCE_ACCOUNT", e.getMessage());
			// verifies that exception was thrown and service was not invoked
			verify(transactionService, never()).transfer(transaction);
		}
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
	
	private com.ingenico.transferservice.persistence.entity.Account getPersistedAccount(String accountName, double amount) {
		com.ingenico.transferservice.persistence.entity.Account account = new com.ingenico.transferservice.persistence.entity.Account();
		account.setName(accountName);
		account.setBalance(amount);
		return account;
	}
	
	private List<Account> getAccounts(String sourceAccountName, double sourceAmount, String targetAccountName, double targetAmount) {
		List<Account> accounts = new ArrayList<>();
		Account sourceAccount = new Account();
		sourceAccount.setName(sourceAccountName);
		sourceAccount.setBalance(sourceAmount);
		
		Account targetAccount = new Account();
		targetAccount.setName(targetAccountName);
		targetAccount.setBalance(targetAmount);
		
		accounts.add(sourceAccount);
		accounts.add(targetAccount);		
		
		return accounts;
	}
	 

}
