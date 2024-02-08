package ru.starkov.struct.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.struct.db.dao.ChatDao;
import ru.starkov.dom.entity.identifier.ChatId;
import ru.starkov.dom.entity.identifier.ChatIdGenerator;

@Service
@RequiredArgsConstructor
public class ChatIdGeneratorImpl implements ChatIdGenerator {

    private final ChatDao chatDao;

    @Override
    public ChatId generate() {
        return new ChatId(chatDao.next());
    }
}
