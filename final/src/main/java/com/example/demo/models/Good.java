package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "goods")
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "good_id")
    private long id;

    private String title;
    private double price;
    private int quantityInStorage;

    @ManyToOne
    private GoodCategory category;

    public Good(String title, double price, int quantityInStorage, GoodCategory category) {
        this.title = title;
        this.price = price;
        this.quantityInStorage = quantityInStorage;
        this.category = category;
    }
}
