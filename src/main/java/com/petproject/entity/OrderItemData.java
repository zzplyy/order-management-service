package com.petproject.entity;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class OrderItemData {

    private Integer quantity;
    private BigDecimal price;

    public OrderItemData() {}

    public OrderItemData(Integer quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
