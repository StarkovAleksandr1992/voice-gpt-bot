package ru.starkov.dom.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.starkov.dom.entity.identifier.TokenAccountId;
import ru.starkov.dom.entity.identifier.TokenAccountIdGenerator;
import ru.starkov.common.mark.Entity;

import java.math.BigInteger;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class TokenAccount implements Entity {

    private final TokenAccountId tokenAccountId;
    private BigInteger balance;

    public static TokenAccount create(@NonNull TokenAccountIdGenerator generator) {
        Objects.requireNonNull(generator, "Token account id generator cannot be null");

        var account = new TokenAccount(generator.generate());

        account.deposit(10_000);
        return account;
    }

    // used by mapper
    public static TokenAccount createNewInstance(
            @NonNull TokenAccountId id,
            @NonNull BigInteger balance) {
        Objects.requireNonNull(id, "Token account id cannot be null");
        Objects.requireNonNull(balance, "Token account balance cannot be null");
        return new TokenAccount(id, balance);
    }

    private TokenAccount(TokenAccountId id) {
        this.tokenAccountId = id;
        this.balance = BigInteger.ZERO;
    }

    private TokenAccount(TokenAccountId id, BigInteger balance) {
        this.tokenAccountId = id;
        this.balance = balance;
    }


    public void withdraw(BigInteger amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(Integer amount) {
        this.balance = this.balance.add(BigInteger.valueOf(amount));
    }

}