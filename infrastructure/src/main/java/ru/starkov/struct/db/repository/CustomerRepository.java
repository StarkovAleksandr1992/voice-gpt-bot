package ru.starkov.struct.db.repository;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.starkov.struct.db.dao.CustomerDao;
import ru.starkov.struct.db.mapper.CustomerDbModelMapper;
import ru.starkov.struct.db.model.CustomerDbModel;
import ru.starkov.dom.entity.Customer;
import ru.starkov.app.port.CustomerGateway;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository implements CustomerGateway {

    private final CustomerDao customerDao;
    private final CustomerDbModelMapper customerDbModelMapper;

    @Transactional
    @Override
    public void save(@NonNull Customer value) {
        customerDao.save(customerDbModelMapper.toModel(value));
    }


    @Transactional
    @Override
    public Optional<Customer> findCustomerByTelegramId(Long customerTelegramId) {
        return customerDao
                .findCustomerDbModelByTelegramId(customerTelegramId)
                .map(customerDbModelMapper::toEntity)
                .or(Optional::empty);
    }

    @Transactional
    public List<CustomerDbModel> findAllCustomers() {
        return customerDao.findAllCustomers();
    }
}