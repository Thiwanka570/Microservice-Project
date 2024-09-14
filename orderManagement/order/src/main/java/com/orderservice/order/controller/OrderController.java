package com.orderservice.order.controller;

import com.orderservice.order.commonHandling.OrderErrorResponce;
import com.orderservice.order.commonHandling.OrderResponce;
import com.orderservice.order.commonHandling.SuccessOrderResponce;
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
            order.setItemId(exsistingOrder.get().getItemId());
            order.setOrderDate(orderDetails.getOrderDate());
            order.setCustomerId(orderDetails.getCustomerId());
            order.setTotalAmount(orderDetails.getTotalAmount());

            Order updatedOrder=orderService.updateOrder(order);
            return new ResponseEntity<>(updatedOrder,HttpStatus.OK);
        }else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/saveOrder")
    public OrderResponce saveOrder(@RequestBody Order orderDetails) {
        if (orderDetails!=null){
            return orderService.saveOrder(orderDetails);
        }else {
            return new OrderErrorResponce("Order details Are Missing");
        }
    }

    @PostMapping
    public OrderResponce creatOrder(@RequestBody Order order){
        return orderService.saveOrder(order);
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
