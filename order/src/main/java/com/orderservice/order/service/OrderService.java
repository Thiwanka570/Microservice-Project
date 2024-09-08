package com.orderservice.order.service;

import com.orderservice.order.entity.Order;
import com.orderservice.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id){
        return orderRepository.findById(id);
    }

    public Order saveOrUpdateOrderById(Order order){
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id){
        orderRepository.deleteById(id);
    }

}
