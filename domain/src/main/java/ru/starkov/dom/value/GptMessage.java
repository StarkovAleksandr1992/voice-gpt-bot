package ru.starkov.dom.value;

public class GptMessage extends Message {
    public GptMessage(String chatMessage) {
        super(ChatRole.GPT, chatMessage);
    }
}
