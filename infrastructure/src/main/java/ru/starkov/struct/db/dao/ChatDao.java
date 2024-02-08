package ru.starkov.struct.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.starkov.struct.db.model.ChatDbModel;

public interface ChatDao extends JpaRepository<ChatDbModel, Long> {
    @Query(nativeQuery = true, value = "SELECT NEXTVAL('public.chat_info_seq')")
    Long next();
}
