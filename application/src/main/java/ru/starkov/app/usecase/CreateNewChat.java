package ru.starkov.app.usecase;

import ru.starkov.app.dto.in.NewChatRequestInfo;

public interface CreateNewChat {
    void execute(NewChatRequestInfo info);
}
