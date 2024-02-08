package ru.starkov.struct.db.repository;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.starkov.struct.db.dao.CustomerRequestDao;
import ru.starkov.struct.db.mapper.CustomerRequestDbModelMapper;
import ru.starkov.struct.db.model.CustomerRequestDbModel;
import ru.starkov.dom.entity.CustomerRequest;
import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.app.port.CustomerRequestGateWay;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRequestRepository implements CustomerRequestGateWay {

    private final CustomerRequestDao customerRequestDao;
    private final CustomerRequestDbModelMapper customerRequestDbModelMapper;

    @Transactional
    @Override
    public void save(@NonNull CustomerRequest value) {
        var model = customerRequestDbModelMapper.toModel(value);
        customerRequestDao.save(model);
    }

    @Transactional
    @Override
    public Optional<CustomerRequest> findById(CustomerRequestId id) {
        return customerRequestDao
                .findById(id.id())
                .map(customerRequestDbModelMapper::toEntity)
                .or(Optional::empty);
    }

    @Transactional
    public List<CustomerRequestDbModel> findAll() {
        return customerRequestDao.findAll();
    }

}
