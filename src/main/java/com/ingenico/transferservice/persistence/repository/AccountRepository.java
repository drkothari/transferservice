package com.ingenico.transferservice.persistence.repository;

import com.ingenico.transferservice.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AccountRepository
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * sample query this method generates : SELECT a FROM Account a WHERE LOWER(a.name) = LOWER(:name)
	 * @param name of the account holder
	 * @return Account the Account entity
	 */
    Account findByName(/*@Param("name")*/ String name);
}
