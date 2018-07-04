package com.ingenico.transferservice.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ingenico.transferservice.persistence.entity.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager; 
    @Autowired
    private AccountRepository accountRepository;
    
    @Test
    public void testFindByName_thenReturnAccount() {
    	// save in in-memory db
    	Account account = new Account();
    	account.setName("Garima");
    	account.setBalance(10004.65);
    	entityManager.persist(account);
    	entityManager.flush();

    	// fetch from db
    	Account persistedAccount = accountRepository.findByName("Garima");
    	
    	// assert check if account is correctly fetched
    	assertNotNull(persistedAccount);
    	assertEquals("Garima", persistedAccount.getName());
    	assertEquals(10004.65, persistedAccount.getBalance(), 0);    	
    }
    
    @Test
    public void testFindByName_thenReturnNull() {
    	// save in in-memory db
    	Account account = new Account();
    	account.setName("Garima");
    	account.setBalance(10004.65);
    	entityManager.persist(account);
    	entityManager.flush();

    	// fetch from db
    	Account persistedAccount = accountRepository.findByName("Aarav");
    	
    	// assert check if account is correctly fetched
    	assertNull(persistedAccount);
    }

}
