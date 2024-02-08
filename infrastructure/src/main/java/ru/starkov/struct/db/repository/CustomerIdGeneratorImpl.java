package ru.starkov.struct.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.dao.CustomerDao;
import ru.starkov.dom.entity.identifier.CustomerId;
import ru.starkov.dom.entity.identifier.CustomerIdGenerator;

@Component
@RequiredArgsConstructor
public class CustomerIdGeneratorImpl implements CustomerIdGenerator {

    private final CustomerDao customerDao;

    @Override
    public CustomerId generate() {
        Long next = customerDao.next();
        return new CustomerId(next);
    }
}
