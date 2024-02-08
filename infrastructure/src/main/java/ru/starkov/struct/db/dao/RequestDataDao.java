package ru.starkov.struct.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.starkov.struct.db.model.RequestDataDbModel;

public interface RequestDataDao extends JpaRepository<RequestDataDbModel<?>, Long> {

    @Query(nativeQuery = true, value = "SELECT NEXTVAL('public.request_data_info_seq')")
    Long next();
}
