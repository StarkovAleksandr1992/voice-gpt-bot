package ru.starkov.struct.db.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_info")
@Getter
@Setter
public class CustomerDbModel {

    @Id
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "telegram_id",
            nullable = false,
            unique = true)
    private Long telegramId;

    @Column(name = "first_name",
            nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "language_code")
    private String languageCode;

    @Embedded
    @Column(name = "settings")
    private CustomerSettingsDbModel customerSettingsDbModel;

    @Column(name = "state")
    private String state;

    @JsonManagedReference
    @OneToMany(mappedBy = "customerDbModel", fetch = FetchType.LAZY)
    private List<CustomerRequestDbModel> requests = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChatDbModel> chats = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "token_account_id")
    private TokenAccountDbModel tokenAccountDbModel;
}