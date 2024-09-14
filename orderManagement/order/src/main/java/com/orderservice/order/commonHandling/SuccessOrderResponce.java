package com.orderservice.order.commonHandling;

import com.orderservice.order.entity.Order;
import lombok.Getter;

@Getter
public class SuccessOrderResponce implements OrderResponce{
    private final Order order;


    public SuccessOrderResponce(Order order) {
        this.order = order;
    }
}
