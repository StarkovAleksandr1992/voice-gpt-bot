package ru.starkov.dom.value;

public class SystemMessage extends Message{
    public SystemMessage(String chatMessage) {
        super(ChatRole.SYSTEM, chatMessage);
    }
}
