package com.driver.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    boolean paymentCompleted;
    PaymentMode paymentMode;
}