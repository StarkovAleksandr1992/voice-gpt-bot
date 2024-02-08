package ru.starkov.struct.db.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.ChatDbModel;
import ru.starkov.struct.db.model.CustomerDbModel;
import ru.starkov.dom.entity.Chat;
import ru.starkov.dom.entity.Customer;
import ru.starkov.dom.entity.identifier.CustomerId;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerDbModelMapper {

    private final CustomerSettingsDbModelMapper customerSettingsDbModelMapper;
    private final ChatDbModelMapper chatDbModelMapper;
    private final TokenAccountDbModelMapper tokenAccountDbModelMapper;

    public CustomerDbModel toModel(@NonNull Customer entity) {
        var id = entity.getCustomerId().id();
        var telegramId = entity.getTelegramId();
        var firstName = entity.getFirstName();
        var lastName = entity.getLastName();
        var languageCode = entity.getLanguageCode();
        var state = entity.getState().toString();
        var customerSettingsDbModel = customerSettingsDbModelMapper.toModel(entity.getCustomerSettings());
        List<ChatDbModel> chatDbModels = new ArrayList<>();
        for (Chat chat : entity.getChats()) {
            chatDbModels.add(chatDbModelMapper.toModel(chat));
        }
        var tokenAccountDbModel = tokenAccountDbModelMapper.toModel(entity.getTokenAccount());

        var model = new CustomerDbModel();
        model.setId(id);
        model.setTelegramId(telegramId);
        model.setFirstName(firstName);
        model.setLastName(lastName);
        model.setLanguageCode(languageCode);
        model.setState(state);
        model.setCustomerSettingsDbModel(customerSettingsDbModel);
        model.setChats(chatDbModels);
        model.setTokenAccountDbModel(tokenAccountDbModel);
        return model;
    }


    public Customer toEntity(@NonNull CustomerDbModel model) {
        var id = new CustomerId(model.getId());
        var telegramId = model.getTelegramId();
        var firstName = model.getFirstName();
        var lastName = model.getLastName();
        var languageCode = model.getLanguageCode();
        var state = Customer.State.valueOf(model.getState());
        var customerSettings = customerSettingsDbModelMapper.toEntity(model.getCustomerSettingsDbModel());
        List<Chat> chats = new ArrayList<>();
        for (ChatDbModel chatDbModel : model.getChats()) {
            chats.add(chatDbModelMapper.toEntity(chatDbModel));
        }
        var tokenAccount = tokenAccountDbModelMapper.toEntity(model.getTokenAccountDbModel());

        return Customer.createNewInstance(id, telegramId, firstName, lastName, languageCode, state, customerSettings, chats, tokenAccount);
    }
}
