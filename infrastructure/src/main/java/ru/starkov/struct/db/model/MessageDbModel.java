package ru.starkov.struct.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MessageDbModel {

    @Column(name = "chat_role")
    private String chatRole;
    @Column(name = "chat_message", length = 4000)
    private String chatMessage;
}
