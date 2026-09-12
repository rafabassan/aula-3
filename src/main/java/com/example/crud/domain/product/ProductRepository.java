package com.example.crud.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findAllByActiveTrue();

    List<Product> findAllByActiveTrueAndDistributionCenter(
            DistributionCenter distributionCenter
    );

    List<Product> findAllByActiveTrueAndCategory(String category);

    List<Product> findAllByActiveTrueAndDistributionCenterAndCategory(
            DistributionCenter distributionCenter,
            String category
    );

    List<Product> findAllByActiveTrueAndPriceGreaterThan(Integer price);

    @Query("""
            SELECT p
            FROM product p
            WHERE p.active = true
            AND LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    List<Product> findAllActiveByNameContaining(String term);

    @Query("""
            SELECT p.distributionCenter as distributionCenter,
                   COUNT(p) as total
            FROM product p
            WHERE p.active = true
            GROUP BY p.distributionCenter
            """)
    List<DistributionCenterCount> countActiveByDistributionCenter();
}
