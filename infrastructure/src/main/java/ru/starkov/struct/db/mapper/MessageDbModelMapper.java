package ru.starkov.struct.db.mapper;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.MessageDbModel;
import ru.starkov.dom.value.CustomerMessage;
import ru.starkov.dom.value.GptMessage;
import ru.starkov.dom.value.Message;
import ru.starkov.dom.value.SystemMessage;

@Component
public class MessageDbModelMapper {

    public MessageDbModel toModel(@NonNull Message entity) {
        var chatRole = entity.getChatRole().toString();
        var chatMessage = entity.getChatMessage();

        MessageDbModel messageDbModel = new MessageDbModel();
        messageDbModel.setChatRole(chatRole);
        messageDbModel.setChatMessage(chatMessage);
        return messageDbModel;
    }

    public Message toEntity(@NonNull MessageDbModel model) {
        var chatRole = Message.ChatRole.valueOf(model.getChatRole());
        var chatMessage = model.getChatMessage();
        Message message;
        if (chatRole == Message.ChatRole.SYSTEM) {
            message = new SystemMessage(chatMessage);
        } else if (chatRole == Message.ChatRole.GPT) {
            message = new GptMessage(chatMessage);
        } else if (chatRole == Message.ChatRole.CUSTOMER) {
            message = new CustomerMessage(chatMessage);
        } else {
            throw new RuntimeException("Invalid chat role");
        }
        return message;
    }
}
