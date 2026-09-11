package com.smartfinance.entities.pk;

import com.smartfinance.entities.Category;
import com.smartfinance.entities.Movement;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovementCategoryPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "movement_id")
    private Movement movement;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovementCategoryPK that = (MovementCategoryPK) o;
        return Objects.equals(movement, that.movement) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movement, category);
    }
}
