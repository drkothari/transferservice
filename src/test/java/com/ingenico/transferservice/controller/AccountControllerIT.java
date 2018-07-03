package com.ingenico.transferservice.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {
	
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	public void testAddAccount() throws Exception {		
		Account account = new Account();
		account.setName("Garima");
		account.setBalance(10000.45);

		HttpEntity<Account> entity = new HttpEntity<>(account, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/accounts"), HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		JSONObject jsonObject = new JSONObject(response.getBody());
		String textStr = jsonObject.toString();
        assertTrue("expected name", textStr.contains("\"name\""));
        assertTrue("expected Garima", textStr.contains("\"Garima\""));
        assertTrue("expected balance", textStr.contains("\"balance\""));
        assertTrue("expected 10000.45", textStr.contains("10000.45"));
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
