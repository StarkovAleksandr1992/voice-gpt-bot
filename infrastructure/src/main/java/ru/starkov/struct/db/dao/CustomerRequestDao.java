package ru.starkov.struct.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.starkov.struct.db.model.CustomerRequestDbModel;

public interface CustomerRequestDao extends JpaRepository<CustomerRequestDbModel, Long> {

    @Query(nativeQuery = true, value = "SELECT NEXTVAL('public.customer_request_info_seq')")
    Long next();

}
