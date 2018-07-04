package com.ingenico.transferservice.orchestrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.service.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class AccountOrchestratorTest {
	
	@InjectMocks
	private AccountOrchestrator accountOrchestrator;
	@Mock
	private AccountService accountService;
	
	@Test
	public void testAddAccount() throws Exception {
		Account account = createAccount();
		Mockito.when(accountService.addAccount(account)).thenReturn(account);
		Account persistedAccount = accountOrchestrator.addAccount(account);
		assertNotNull(persistedAccount);
		assertEquals("Garima", persistedAccount.getName());		
	}
	
	private Account createAccount() {
		Account modelAccount = new Account();
		modelAccount.setName("Garima");
		modelAccount.setBalance(1000.55);
		return modelAccount;
	}

}
