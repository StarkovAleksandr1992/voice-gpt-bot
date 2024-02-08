package ru.starkov.dom.value;

import lombok.Data;
import lombok.Getter;
import ru.starkov.common.mark.ValueObject;

@Data
public abstract class Message implements ValueObject {

    private final ChatRole chatRole;
    private final String chatMessage;

    @Getter
    public enum ChatRole {
        SYSTEM("system"),
        CUSTOMER("user"),
        GPT("assistant");
        private final String senderName;

        ChatRole(String senderName) {
            this.senderName = senderName;
        }

    }
}
