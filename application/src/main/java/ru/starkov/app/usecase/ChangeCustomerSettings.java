package ru.starkov.app.usecase;

import ru.starkov.app.dto.in.CustomerSettingsActionTypeInfo;
import ru.starkov.app.dto.in.CustomerSettingsGptModelInfo;

public interface ChangeCustomerSettings {
    void changeDefaultActionType(CustomerSettingsActionTypeInfo actionTypeInfo);
    void changeDefaultGptModel(CustomerSettingsGptModelInfo gptModelInfo);
}
