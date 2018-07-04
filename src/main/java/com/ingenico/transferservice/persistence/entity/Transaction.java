package com.ingenico.transferservice.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transaction Entity Class
 */
@Entity
public class Transaction implements Serializable{

	private static final long serialVersionUID = 2L;

	@Id
    @GeneratedValue
    private Integer id;

    private double amount;

    @OneToOne
    @JoinColumn(name = "SOURCE_ACCOUNT_ID", nullable = false)
    private Account sourceAccount;

    @OneToOne
    @JoinColumn(name = "TARGET_ACCOUNT_ID", nullable = false)
    private Account targetAccount;

    private LocalDateTime created_on;

    private LocalDateTime updated_on;

    public Transaction() {
    }

    public Transaction(com.ingenico.transferservice.model.Transaction transactionModel, Account sourceAccount, Account targetAccount) {
        if (transactionModel != null) {
            this.amount = transactionModel.getAmount();
            this.sourceAccount = sourceAccount;
            this.targetAccount = targetAccount;
            this.created_on = LocalDateTime.now();
            this.updated_on = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    public LocalDateTime getCreated_on() {
        return created_on;
    }

    public void setCreated_on(LocalDateTime created_on) {
        this.created_on = created_on;
    }

    public LocalDateTime getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(LocalDateTime updated_on) {
        this.updated_on = updated_on;
    }
}
