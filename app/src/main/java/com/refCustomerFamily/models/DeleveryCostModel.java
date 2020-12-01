package com.refCustomerFamily.models;

import java.io.Serializable;

public class DeleveryCostModel implements Serializable {

    private int delivery_cost;


    DeleveryCostModel() {
    }

    public DeleveryCostModel(int delivery_cost) {
        this.delivery_cost = delivery_cost;

    }

    public int getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(int delivery_cost) {
        this.delivery_cost = delivery_cost;
    }
}
