package ru.starkov.struct.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "data_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "request_data_info")
@Getter
@Setter
public abstract class RequestDataDbModel<T> {

    @Id
    @Column(name = "request_data_id")
    private Long id;
    @Column(name = "data_type", insertable = false, updatable = false)
    private String dataType;

    public abstract T getData();

    public abstract void setData(T data);
}
