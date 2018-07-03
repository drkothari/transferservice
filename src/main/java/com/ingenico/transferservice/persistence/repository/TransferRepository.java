package com.ingenico.transferservice.persistence.repository;

import com.ingenico.transferservice.persistence.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
