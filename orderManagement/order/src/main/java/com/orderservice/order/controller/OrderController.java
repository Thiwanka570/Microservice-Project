package com.orderservice.order.controller;

import com.orderservice.order.entity.Order;
import com.orderservice.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(Long id) {
        Optional<Order> order = orderService.findOrderById(id);
        return order.map(vale -> new ResponseEntity<>(vale, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> UpdateOrder(@PathVariable("id") Long id,@RequestBody Order orderDetails) {
        Optional<Order> exsistingOrder = orderService.findOrderById(id);
        if (exsistingOrder.isPresent()){
            Order order=exsistingOrder.get();
            order.setOrderDate(orderDetails.getOrderDate());
            order.setCustomerId(orderDetails.getCustomerId());
            order.setTotalAmount(orderDetails.getTotalAmount());

            Order updatedOrder=orderService.saveOrUpdateOrderById(order);
            return new ResponseEntity<>(updatedOrder,HttpStatus.OK);
        }else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Order> creatOrder(@RequestBody Order order){
        Order saveOrder=orderService.saveOrUpdateOrderById(order);
        return new ResponseEntity<>(saveOrder,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        Optional<Order> existantOrder=orderService.findOrderById(id);
        if (existantOrder.isPresent()){
            orderService.deleteOrderById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
