package com.fteam.supportapp;

/**
 * Created by N1cK0 on 15/06/05.
 */
public class Message {
    private String message;
    private boolean user;

    public Message(String message, boolean user){
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public boolean getUser(){
        return user;
    }
}