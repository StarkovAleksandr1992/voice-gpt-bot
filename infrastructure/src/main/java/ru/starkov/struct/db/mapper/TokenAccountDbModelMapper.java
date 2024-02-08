package ru.starkov.struct.db.mapper;

import org.springframework.stereotype.Component;
import ru.starkov.struct.db.model.TokenAccountDbModel;
import ru.starkov.dom.entity.TokenAccount;
import ru.starkov.dom.entity.identifier.TokenAccountId;

@Component
public class TokenAccountDbModelMapper {

    public TokenAccountDbModel toModel(TokenAccount entity) {
        var id = entity.getTokenAccountId().id();
        var balance = entity.getBalance();

        TokenAccountDbModel model = new TokenAccountDbModel();
        model.setId(id);
        model.setBalance(balance);
        return model;
    }

    public TokenAccount toEntity(TokenAccountDbModel model) {
        var id = model.getId();
        var balance = model.getBalance();

        return TokenAccount.createNewInstance(new TokenAccountId(id), balance);
    }
}
