package ru.starkov.struct.db.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "chat_info")
@Data
public class ChatDbModel {

    @Id
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "status")
    private String status;

    @ElementCollection
    @CollectionTable(name = "chat_messages")
    private List<MessageDbModel> messages;

}

