package com.example.pfm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Goal {

    @Id @GeneratedValue
    private Long id;

    private String goalName;
    private BigDecimal targetAmount;
    private LocalDate targetDate;
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
