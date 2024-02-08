package ru.starkov.struct.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.starkov.struct.db.dao.CustomerRequestDao;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.dom.entity.identifier.CustomerRequestIdGenerator;

@Service
@RequiredArgsConstructor
public class CustomerRequestIdGeneratorImpl implements CustomerRequestIdGenerator {

    private final CustomerRequestDao customerRequestDao;

    @Override
    public CustomerRequestId generate() {
        Long next = customerRequestDao.next();
        return new CustomerRequestId(next);
    }
}