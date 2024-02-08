package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.dto.in.NewChatRequestInfo;
import ru.starkov.dom.entity.identifier.ChatIdGenerator;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.app.usecase.CreateNewChat;


@Service
@RequiredArgsConstructor
public class CreateNewChatImpl implements CreateNewChat {

    private final CustomerGateway customerGateway;
    private final ChatIdGenerator chatIdGenerator;
    private final CustomerNotificationService customerNotificationService;

    @Override
    public void execute(NewChatRequestInfo info) {
        var customerOptional = customerGateway.findCustomerByTelegramId(info.customerTelegramId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Could not find a customer with the provided Telegram ID");
        }
        var customer = customerOptional.get();
        customer.createNewChat(chatIdGenerator);
        customerGateway.save(customer);
        customerNotificationService.notifyAboutNewChatCreatedSuccessfully(info);
    }
}
