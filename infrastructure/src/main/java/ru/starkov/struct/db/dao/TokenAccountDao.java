package ru.starkov.struct.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.starkov.struct.db.model.TokenAccountDbModel;

public interface TokenAccountDao extends JpaRepository<TokenAccountDbModel, Long> {

    @Query(nativeQuery = true, value = "SELECT NEXTVAL('public.token_account_info_seq')")
    Long next();
}
