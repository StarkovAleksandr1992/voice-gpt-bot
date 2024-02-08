package ru.starkov.struct.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Table(name = "token_account_info")
@Data
public class TokenAccountDbModel {

    @Id
    @Column(name = "token_account_id")
    private Long id;

    @Column(name = "balance")
    private BigInteger balance;
}
