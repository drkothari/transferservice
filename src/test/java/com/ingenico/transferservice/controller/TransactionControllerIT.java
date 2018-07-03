package com.ingenico.transferservice.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ingenico.transferservice.TransferServiceApplication;
import com.ingenico.transferservice.model.Account;
import com.ingenico.transferservice.model.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIT {
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testTransferFundAccepted() throws Exception {
		// add the acocunts first using addAccounts service
		addAccount("Garima", 2000.45d, "Gudiya", 1000.45d);
		
		Transaction transaction = new Transaction();
		Account sourceAccount = new Account();
		sourceAccount.setName("Garima");
		transaction.setSourceAccount(sourceAccount);
		Account targetAccount = new Account();
		targetAccount.setName("Gudiya");
		transaction.setTargetAccount(targetAccount);
		double amount = 100.00d;
		transaction.setAmount(amount);
		
		HttpEntity<Transaction> entity = new HttpEntity<>(transaction, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		
		JSONArray jsonArray = new JSONArray(response.getBody());
		String textStr = jsonArray.get(0).toString();
        assertTrue("expected name", textStr.contains("\"name\""));
        assertTrue("expected Garima", textStr.contains("\"Garima\""));
        assertTrue("expected balance", textStr.contains("\"balance\""));
        assertTrue("expected 1900.45", textStr.contains("1900.45"));
        
        textStr = jsonArray.get(1).toString();
        assertTrue("expected name", textStr.contains("\"name\""));
        assertTrue("expected Gudiya", textStr.contains("\"Gudiya\""));
        assertTrue("expected balance", textStr.contains("\"balance\""));
        assertTrue("expected 1100.45", textStr.contains("1100.45"));
        
	}

	@Test
	public void testTransferFund_FailureCase1() throws Exception {
		Transaction transaction = new Transaction();
		Account sourceAccount = new Account();
		sourceAccount.setName("Garima1");
		transaction.setSourceAccount(sourceAccount);
		Account targetAccount = new Account();
		targetAccount.setName("Gudiya1");
		transaction.setTargetAccount(targetAccount);
		double amount = 100.00d;
		transaction.setAmount(amount);
		
		HttpEntity<Transaction> entity = new HttpEntity<>(transaction, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("TARGET_ACCOUNT_NOT_FOUND", response.getBody());
	}

	@Test
	public void testTransferFund_FailureCase2() throws Exception {
		//add only one acocunt
		addOneAccount(2000.45d);
		
		Transaction transaction = new Transaction();
		Account sourceAccount = new Account();
		sourceAccount.setName("Garima2");
		transaction.setSourceAccount(sourceAccount);
		Account targetAccount = new Account();
		targetAccount.setName("Gudiya2");
		transaction.setTargetAccount(targetAccount);
		double amount = 100.00d;
		transaction.setAmount(amount);
		
		HttpEntity<Transaction> entity = new HttpEntity<>(transaction, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("SOURCE_ACCOUNT_NOT_FOUND", response.getBody());
	}
	
	@Test
	public void testTransferFund_FailureInsufficientBalance() throws Exception {
		//add only one acocunt
		addAccount("Garima4", 100.45d, "Gudiya4", 200.45d);
		
		Transaction transaction = new Transaction();
		Account sourceAccount = new Account();
		sourceAccount.setName("Garima4");
		transaction.setSourceAccount(sourceAccount);
		Account targetAccount = new Account();
		targetAccount.setName("Gudiya4");
		transaction.setTargetAccount(targetAccount);
		double amount = 1000.00d;
		transaction.setAmount(amount);
		
		HttpEntity<Transaction> entity = new HttpEntity<>(transaction, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("INSUFFICIENT_BALANCE_IN_SOURCE_ACCOUNT", response.getBody());
	}


	private void addOneAccount(double targetAccountBalance) {
		Account account1 = new Account();
		account1.setName("Gudiya2");
		account1.setBalance(targetAccountBalance);

		HttpEntity<Account> entity = new HttpEntity<>(account1, headers);
		restTemplate.exchange(createURLWithPort("/accounts"), HttpMethod.POST, entity, String.class);
	}

	
	private void addAccount(String sourceName, double sourceAccountBalance, String targetName, double targetAccountBalance) {
		Account account1 = new Account();
		account1.setName(sourceName);
		account1.setBalance(sourceAccountBalance);

		HttpEntity<Account> entity = new HttpEntity<>(account1, headers);
		restTemplate.exchange(createURLWithPort("/accounts"), HttpMethod.POST, entity, String.class);
		
		Account account2 = new Account();
		account2.setName(targetName);
		account2.setBalance(targetAccountBalance);
		
		entity = new HttpEntity<>(account2, headers);
		restTemplate.exchange(createURLWithPort("/accounts"), HttpMethod.POST, entity, String.class);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}


}
