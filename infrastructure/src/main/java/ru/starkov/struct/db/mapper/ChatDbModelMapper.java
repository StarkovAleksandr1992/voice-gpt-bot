package ru.starkov.struct.db.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.ChatDbModel;
import ru.starkov.struct.db.model.MessageDbModel;
import ru.starkov.dom.entity.Chat;
import ru.starkov.dom.entity.identifier.ChatId;
import ru.starkov.dom.value.Message;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatDbModelMapper {

    private final MessageDbModelMapper messageDbModelMapper;

    public ChatDbModel toModel(@NonNull Chat entity) {
        var model = new ChatDbModel();
        var id = entity.getChatId().id();
        var status = entity.getStatus().toString();
        List<Message> chatHistory = entity.getChatHistory();
        List<MessageDbModel> chatHistoryDbModel = new ArrayList<>();
        if (chatHistory != null) {
            for (Message message : chatHistory) {
                chatHistoryDbModel.add(messageDbModelMapper.toModel(message));
            }
        }
        model.setId(id);
        model.setStatus(status);
        model.setMessages(chatHistoryDbModel);
        return model;
    }

    public Chat toEntity(@NonNull ChatDbModel model) {
        var chatId = new ChatId(model.getId());
        var chat = new Chat(chatId);
        chat.setStatus(Chat.Status.valueOf(model.getStatus()));
        List<MessageDbModel> chatHistoryDbModel = model.getMessages();
        List<Message> chatHistory = new ArrayList<>();
        if (chatHistoryDbModel != null && !chatHistoryDbModel.isEmpty()) {
            for (MessageDbModel messageDbModel : chatHistoryDbModel) {
                chatHistory.add(messageDbModelMapper.toEntity(messageDbModel));
            }
        }
        chat.setChatHistory(chatHistory);
        return chat;
    }
}
