package com.example.securedchat.Models;

import java.util.UUID;

public class Message {

    private String description;
    private String messageId;
    private String senderName;
    private int position;

    public Message(String description, String senderName , int position) {
        this.description = description;
        this.messageId = UUID.randomUUID().toString();
        this.senderName = senderName;
        this.position = position;
    }

    public Message() {
    }

    public String getDescription() {
        return description;
    }

    public String getMessageId() {
        return messageId;
    }


    public String getSenderName() {
        return senderName;
    }



    public int getPosition() {
        return position;
    }

}
