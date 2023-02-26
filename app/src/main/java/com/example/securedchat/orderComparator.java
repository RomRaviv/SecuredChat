package com.example.securedchat;

import com.example.securedchat.Models.Message;

import java.util.Comparator;

public class orderComparator implements Comparator<Message> {
    @Override
    public int compare(Message m1, Message m2) {
        if (m1.getPosition()>m2.getPosition())
            return 1;
        return -1;
    }

}
