package com.ingenico.transferservice.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import com.ingenico.transferservice.context.TransferServiceException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Matchers.any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.persistence.repository.AccountRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest {
	
	@TestConfiguration
    static class AccountServiceTestContextConfiguration {
  
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }
    }
	
	@Autowired
	private AccountService accountService;
	
	@MockBean
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
