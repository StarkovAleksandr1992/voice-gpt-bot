package ru.starkov.struct.db.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Table(name = "customer_request_info")
@Getter
@Setter
public class CustomerRequestDbModel {

    @Id
    @Column(name = "customer_request_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private CustomerDbModel customerDbModel;

    @Column(name = "state")
    private String state;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_data_id")
    private RequestDataDbModel<?> requestDataDbModel;

    @Column(name = "result_data", length = 4000)
    private String resultData;

    @Column(name = "token_cost")
    private BigInteger tokenCost;

}