package com.example.pfm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
public class Category {

    public enum Type {
        INCOME, EXPENSE;

        @com.fasterxml.jackson.annotation.JsonCreator
        public static Type from(String s){
            return Arrays.stream(values())
                    .filter(t -> t.name().equalsIgnoreCase(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("type"));
        }
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;
    @Enumerated(EnumType.STRING) private Type type;
    private boolean custom;
    @ManyToOne(fetch = FetchType.LAZY) private User user;
}
