package ru.starkov.dom.entity;

import lombok.*;
import ru.starkov.dom.entity.identifier.TokenAccountIdGenerator;
import ru.starkov.dom.entity.identifier.ChatIdGenerator;
import ru.starkov.common.event.ResultWithEvent;
import ru.starkov.dom.event.CustomerCreatedEvent;
import ru.starkov.dom.entity.identifier.CustomerId;
import ru.starkov.dom.entity.identifier.CustomerIdGenerator;
import ru.starkov.common.mark.Entity;
import ru.starkov.dom.value.CustomerSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public class Customer implements Entity {

    private final CustomerId customerId;
    private final Long telegramId;
    private final String firstName;
    private final String lastName;
    private final String languageCode;
    private final TokenAccount tokenAccount;

    private State state;
    private List<CustomerRequest> requests;
    private CustomerSettings customerSettings;
    private List<Chat> chats;


    public static ResultWithEvent<Customer, CustomerCreatedEvent> create(
            @NonNull CustomerIdGenerator generator,
            @NonNull Long customerTelegramId,
            String firstName,
            String lastName,
            String languageCode,
            @NonNull TokenAccountIdGenerator tokenAccountIdGenerator) {

        Objects.requireNonNull(generator, "Customer id generator cannot be null");
        Objects.requireNonNull(customerTelegramId, "Customer telegram Id cannot be null");
        Objects.requireNonNull(tokenAccountIdGenerator, "Token account id generator cannot be null");

        var id = generator.generate();
        var customer = new Customer(id, customerTelegramId, firstName, lastName, languageCode, TokenAccount.create(tokenAccountIdGenerator));
        customer.state = State.ACTIVE;
        customer.requests = new ArrayList<>();
        customer.chats = new ArrayList<>();
        customer.customerSettings = CustomerSettings.createDefaultSettings();
        return ResultWithEvent.of(customer, new CustomerCreatedEvent(customer.getCustomerId()));
    }


    public static Customer createNewInstance(
            @NonNull CustomerId customerId,
            @NonNull Long telegramId,
            @NonNull String firstName,
            String lastName,
            @NonNull String languageCode,
            @NonNull State state,
            @NonNull CustomerSettings customerSettings,
            List<Chat> chats,
            @NonNull TokenAccount tokenAccount) {

        Objects.requireNonNull(customerId, "Customer Id cannot be null");
        Objects.requireNonNull(telegramId, "Telegram Id cannot be null");
        Objects.requireNonNull(firstName, "First Name cannot be null");
        Objects.requireNonNull(languageCode, "Language Code cannot be null");
        Objects.requireNonNull(state, "State cannot be null");
        Objects.requireNonNull(customerSettings, "Customer Settings cannot be null");
        Objects.requireNonNull(tokenAccount, "Token account cannot be null");

        return new Customer(customerId, telegramId, firstName, lastName, languageCode, state, customerSettings, chats, tokenAccount);
    }

    public Chat createNewChat(@NonNull ChatIdGenerator generator) {
        this.chats.stream()
                .filter(chat -> chat.getStatus() == Chat.Status.ACTIVE)
                .forEach(chat -> chat.setStatus(Chat.Status.CLOSED));
        this.chats.add(Chat.create(generator));
        Optional<Chat> activeChat = chats.stream()
                .filter(chat -> chat.getStatus() == Chat.Status.ACTIVE)
                .findFirst();
        if (activeChat.isPresent()) {
            return activeChat.get();
        } else {
            throw new RuntimeException("No active chat found");
        }
    }

    private Customer(CustomerId customerId,
                     Long telegramId,
                     String firstName,
                     String lastName,
                     String languageCode,
                     TokenAccount tokenAccount) {
        this.customerId = customerId;
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.languageCode = languageCode;
        this.tokenAccount = tokenAccount;
    }

    private Customer(CustomerId customerId,
                     Long telegramId,
                     String firstName,
                     String lastName,
                     String languageCode,
                     State state,
                     CustomerSettings customerSettings,
                     List<Chat> chats,
                     TokenAccount tokenAccount) {
        this.customerId = customerId;
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.languageCode = languageCode;
        this.state = state;
        this.customerSettings = customerSettings;
        this.chats = chats;
        this.tokenAccount = tokenAccount;
    }

    @ToString
    public enum State {
        ACTIVE,
        INACTIVE,
        DISABLE
    }
}