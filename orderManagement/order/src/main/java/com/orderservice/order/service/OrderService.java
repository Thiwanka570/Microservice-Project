package com.orderservice.order.service;

import com.inventryService.inventry.entity.Inventory;
import com.orderservice.order.commonHandling.OrderErrorResponce;
import com.orderservice.order.commonHandling.OrderResponce;
import com.orderservice.order.commonHandling.SuccessOrderResponce;
import com.orderservice.order.entity.Order;
import com.orderservice.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final WebClient webClient;
    private int itemQty;
    private Inventory inventory;


    @Autowired
    OrderRepository orderRepository;

    public OrderService(WebClient.Builder webClientBuilder,OrderRepository orderRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8004").build();
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();

    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public OrderResponce saveOrder(Order order) {
        System.out.println("testing 1");
        Long itemId = order.getItemId();
        try {
            Inventory inventoryResponce = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/inventory/{id}")  // Corrected the path here
                            .build(itemId))  // Builds the full path with itemId
                    .retrieve()
                    .bodyToMono(Inventory.class)
                    .block();

            System.out.println(inventoryResponce);
            assert inventoryResponce != null;
            int itemQty = inventoryResponce.getQuantity();

            if (itemQty > 0) {
                System.out.println("itemQty = " + itemQty);
                Order savedOrder = orderRepository.save(order);
                inventoryItemQtyUpdate(inventoryResponce);

                return new SuccessOrderResponce(savedOrder);
            } else {
                return new OrderErrorResponce("Item Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void inventoryItemQtyUpdate(Inventory inventory){
        itemQty=itemQty-1;
        inventory.setQuantity(itemQty);

        try {
            Inventory inventoryResponce = webClient.post()
                    .uri("/api/inventory")
                    .bodyValue(inventory)
                    .retrieve()
                    .bodyToMono(Inventory.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    private int getItemQty() {
        return itemQty;
    }

    private void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    private Inventory getInventory() {
        return inventory;
    }

    private void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
