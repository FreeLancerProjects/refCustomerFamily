package com.refCustomerFamily.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private String id;
    private int room_id;



    public ChatUserModel(String name, String image, String id, int room_id) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.room_id = room_id;

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


}
