package com.ingenico.transferservice.persistence.repository;

import com.ingenico.transferservice.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByName(String name);
}
