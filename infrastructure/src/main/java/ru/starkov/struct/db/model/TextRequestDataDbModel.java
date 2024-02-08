package ru.starkov.struct.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("text")
@Getter
@Setter
public class TextRequestDataDbModel extends RequestDataDbModel<String> {

    @Column(name = "string_data", length = 2550)
    private String data;

    @Column(name = "prompt", length = 1450)
    private String prompt;


    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }
}
