package com.ingenico.transferservice.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private double balance;

    private LocalDateTime created_on;

    private LocalDateTime updated_on;

    public Account() {
    }

    public Account(com.ingenico.transferservice.model.Account accountModel) {
        if (accountModel != null) {
            this.name = accountModel.getName();
            this.balance = accountModel.getBalance();
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
