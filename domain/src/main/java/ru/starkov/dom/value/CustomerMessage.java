package ru.starkov.dom.value;


public class CustomerMessage extends Message {

    public CustomerMessage(String chatMessage) {
        super(ChatRole.CUSTOMER, chatMessage);
    }
}
