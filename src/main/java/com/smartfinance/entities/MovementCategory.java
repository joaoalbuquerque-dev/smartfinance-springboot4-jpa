package com.smartfinance.entities;

import com.smartfinance.entities.pk.MovementCategoryPK;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_movement_category")
public class MovementCategory implements Serializable {

    @EmbeddedId
    private MovementCategoryPK id;

    private Double amount;

    public MovementCategory() {
    }

    public MovementCategory(Movement movement, Category category, Double amount) {
        id.setMovement(movement);
        id.setCategory(category);
        this.amount = amount;
    }

    public Movement getMovement() {
        return id.getMovement();
    }

    public void setMovement(Movement movement) {
        id.setMovement(movement);
    }

    public Category getCategory() {
        return id.getCategory();
    }

    public void setCategory(Category category) {
        id.setCategory(category);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovementCategory that = (MovementCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
