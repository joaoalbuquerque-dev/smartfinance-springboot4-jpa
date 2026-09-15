package com.smartfinance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartfinance.entities.enums.TransactionType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_movement")
public class Movement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double amount;
    private Instant transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "id.movement")
    private Set<MovementCategory> movementCategories = new HashSet<>();

    public Movement() {
    }

    public Movement(Long id, String description, Double amount, Instant transactionDate, TransactionType transactionType, Account account) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<MovementCategory> getMovementCategories() {
        return movementCategories;
    }

    public void setMovementCategories(Set<MovementCategory> movementCategories) {
        this.movementCategories = movementCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(id, movement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
