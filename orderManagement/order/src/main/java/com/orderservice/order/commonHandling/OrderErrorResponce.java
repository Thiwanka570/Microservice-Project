package com.orderservice.order.commonHandling;

import lombok.Getter;

@Getter
public class OrderErrorResponce implements OrderResponce{
    private final String errormessage;

    public OrderErrorResponce(String errormessage) {
        this.errormessage = errormessage;
    }
}
