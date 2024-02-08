package ru.starkov.struct.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.struct.db.dao.TokenAccountDao;
import ru.starkov.dom.entity.identifier.TokenAccountId;
import ru.starkov.dom.entity.identifier.TokenAccountIdGenerator;

@Service
@RequiredArgsConstructor
public class TokenAccountIdGeneratorImpl implements TokenAccountIdGenerator {

    private final TokenAccountDao tokenAccountDao;

    @Override
    public TokenAccountId generate() {
        return new TokenAccountId(tokenAccountDao.next());
    }
}
