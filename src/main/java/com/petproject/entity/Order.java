package com.petproject.entity;

import com.petproject.model.OrderState;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderState orderStatus;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product, OrderItemData> orderItems = new HashMap<>();

    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(OrderState orderStatus, Client client) {
        this.orderStatus = orderStatus;
        this.client = client;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderState getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderState orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<Product, OrderItemData> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<Product, OrderItemData> orderItems) {
        this.orderItems = orderItems;
    }
}
