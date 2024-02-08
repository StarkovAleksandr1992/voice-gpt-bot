package ru.starkov.app.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.app.usecase.ChangeCustomerSettings;
import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;
import ru.starkov.app.port.CustomerGateway;
import ru.starkov.app.port.CustomerNotificationService;
import ru.starkov.dom.value.CustomerSettings;


@Service
@RequiredArgsConstructor
public class ChangeCustomerSettingsImpl implements ChangeCustomerSettings {

    private final CustomerGateway customerGateway;
    private final CustomerNotificationService notificationService;

    @Override
    public void changeDefaultActionType(CustomerSettingsActionTypeInfo actionTypeInfo) {
        var customerOptional = customerGateway.findCustomerByTelegramId(actionTypeInfo.customerTelegramId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Could not find a customer with the provided Telegram ID");
        }
        var customer = customerOptional.get();
        var settings = customer.getCustomerSettings().changeDefaultActionType(CustomerSettings.ActionType.valueOf(actionTypeInfo.actionType().name()));
        customerGateway.save(customer);
        notificationService.notifyAboutSuccessfulSettingsActionTypeChange(new CustomerSettingsActionTypeInfo(customer.getTelegramId(),
                CustomerSettingsActionTypeInfo.ActionType.valueOf(settings.getDefaultActionType().name())));
    }

    @Override
    public void changeDefaultGptModel(CustomerSettingsGptModelInfo gptModelInfo) {
        var customerOptional = customerGateway.findCustomerByTelegramId(gptModelInfo.customerTelegramId());
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Could not find a customer with the provided Telegram ID");
        }
        var customer = customerOptional.get();
        var settings = customer.getCustomerSettings().changeDefaultGptModel(CustomerSettings.GptModel.valueOf(gptModelInfo.gptModel().name()));
        customerGateway.save(customer);
        notificationService.notifyAboutSuccessfulSettingsGptModelChange(new CustomerSettingsGptModelInfo(customer.getTelegramId(),
                CustomerSettingsGptModelInfo.GptModel.valueOf(settings.getDefaultGptModel().name())));
    }
}
