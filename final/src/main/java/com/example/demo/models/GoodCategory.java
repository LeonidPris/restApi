package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "category")
public class GoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    private String categoryName;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @Transient
    private List<Good> goods;

    public GoodCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
