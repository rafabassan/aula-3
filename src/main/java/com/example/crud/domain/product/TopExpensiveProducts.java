package com.example.crud.domain.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopExpensiveProducts {

    private final List<Product> products;

    public TopExpensiveProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> calculate() {
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .toList();
    }
}
