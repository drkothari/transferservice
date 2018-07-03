package com.ingenico.transferservice.persistence.repository;

import com.ingenico.transferservice.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    //@Query("SELECT a FROM Account a WHERE LOWER(a.name) = LOWER(:name)")
    Account findByName(/*@Param("name")*/ String name);
}
