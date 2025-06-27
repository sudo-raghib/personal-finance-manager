package com.example.pfm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {

    @Id @GeneratedValue private Long id;
    private BigDecimal amount;
    private LocalDate  date;
    private String     description;

    @Enumerated(EnumType.STRING) private Category.Type type;

    @ManyToOne(fetch = FetchType.LAZY) private Category category;
    @ManyToOne(fetch = FetchType.LAZY) private User     user;

    @Builder.Default
    private boolean deleted = false;
}