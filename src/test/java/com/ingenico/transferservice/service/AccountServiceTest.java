package com.ingenico.transferservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ingenico.transferservice.context.TransferServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
	
	@InjectMocks
	private AccountService accountService;	
	@Mock
    private AccountRepository accountRepository;
	
	@Test
	public void testAddAccount() throws TransferServiceException{
		Account modelAccount = new Account();
		modelAccount.setName("Garima");
		modelAccount.setBalance(1000.55);
		
		com.ingenico.transferservice.persistence.entity.Account account = new com.ingenico.transferservice.persistence.entity.Account();
		account.setName("Garima");
		account.setBalance(1002.55);		
		
		Mockito.when(accountRepository.save(any())).thenReturn(account);
		Account persistedAccount = accountService.addAccount(modelAccount);
		
		assertNotNull(persistedAccount);
		assertEquals("Garima", persistedAccount.getName());
	}

}
