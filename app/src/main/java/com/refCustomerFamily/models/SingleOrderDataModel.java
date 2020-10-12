package com.refCustomerFamily.models;

import java.io.Serializable;

public class SingleOrderDataModel implements Serializable {
    private OrderModel.Data order;

    public OrderModel.Data getOrder() {
        return order;
    }
}
