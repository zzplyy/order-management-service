package com.petproject.controller;

import com.petproject.entity.Client;
import com.petproject.entity.Order;
import com.petproject.exception.ResourceNotFoundException;
import com.petproject.model.OrderState;
import com.petproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Создание нового заказа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestParam Long clientId, @RequestParam OrderState orderStatus) {
        Client client = new Client();
        Order newOrder = new Order(orderStatus, client);  // передать соответствующего клиента
        return orderRepository.save(newOrder);
    }

    // Получение всех заказов
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получение заказа по ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    // Обновление статуса заказа
    @PutMapping("/{id}")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam OrderState newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(newStatus);
            return orderRepository.save(order);
        } else {
            throw new ResourceNotFoundException("Order not found");
        }
    }
    // Удаление заказа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }
}
