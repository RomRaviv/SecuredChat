package com.example.securedchat.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message {

    private String description;
    private String messageId;
    private String senderName;
    private String date;
    private int position;

    public Message(String description, String senderName, int position) {
        this.description = description;
        this.messageId = UUID.randomUUID().toString();
        this.senderName = senderName;
        this.position = position;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        this.date = dateFormat.format(currentDate);
    }

    public Message() {
    }

    public String getDescription() {
        return description;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getDate() {
        return date;
    }

    public int getPosition() {
        return position;
    }

}
