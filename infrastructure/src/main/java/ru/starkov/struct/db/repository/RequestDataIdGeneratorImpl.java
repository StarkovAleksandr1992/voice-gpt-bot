package ru.starkov.struct.db.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.starkov.struct.db.dao.RequestDataDao;
import ru.starkov.dom.entity.identifier.RequestDataId;
import ru.starkov.dom.entity.identifier.RequestDataIdGenerator;

@Component
@RequiredArgsConstructor
public class RequestDataIdGeneratorImpl implements RequestDataIdGenerator {

    private final RequestDataDao requestDataDao;

    @Override
    public RequestDataId generate() {
        return new RequestDataId(requestDataDao.next());
    }
}
