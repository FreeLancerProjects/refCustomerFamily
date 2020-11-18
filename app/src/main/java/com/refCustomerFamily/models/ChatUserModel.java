package com.refCustomerFamily.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private String id;
    private int room_id;
    private int order_id;
    private String type;

    public ChatUserModel(String name, String image, String id, int room_id, int order_id, String type) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.room_id = room_id;
        this.order_id = order_id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getType() {
        return type;
    }
}
