package ru.starkov.struct.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.starkov.struct.db.model.CustomerDbModel;

import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<CustomerDbModel, Long> {

    @Query(nativeQuery = true, value = "SELECT NEXTVAL('public.customer_info_seq')")
    Long next();

    Optional<CustomerDbModel> findCustomerDbModelByTelegramId(Long telegramId);

    @Query("SELECT DISTINCT c FROM CustomerDbModel c JOIN FETCH c.requests")
    List<CustomerDbModel> findAllCustomers();
}
