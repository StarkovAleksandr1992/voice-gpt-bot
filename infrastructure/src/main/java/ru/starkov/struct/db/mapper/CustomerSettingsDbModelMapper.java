package ru.starkov.struct.db.mapper;

import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.CustomerSettingsDbModel;
import ru.starkov.dom.value.CustomerSettings;

@Component
public class CustomerSettingsDbModelMapper {

    public CustomerSettingsDbModel toModel(CustomerSettings entity) {
        var defaultActionType = entity.getDefaultActionType().toString();
        var modelUrl = entity.getDefaultGptModel().getModelUrl();
        CustomerSettingsDbModel model = new CustomerSettingsDbModel();
        model.setActionType(defaultActionType);
        model.setGptModel(modelUrl);
        return model;
    }

    public CustomerSettings toEntity(CustomerSettingsDbModel model) {
        var actionType = CustomerSettings.ActionType.valueOf(model.getActionType());
        CustomerSettings.GptModel gptModel = CustomerSettings.GptModel.fromModelUrl(model.getGptModel());
        return CustomerSettings.createNewInstance(actionType, gptModel);
    }
}
