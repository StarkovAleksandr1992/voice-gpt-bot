package ru.starkov.struct.db.model;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class CustomerSettingsDbModel {

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "gpt_model")
    private String gptModel;
}
