package com.example.crud.controllers;

import com.example.crud.domain.product.DistributionCenter;
import com.example.crud.domain.product.DistributionCenterCount;
import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestProduct;
import com.example.crud.domain.product.TopExpensiveProducts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        var allProducts = repository.findAllByActiveTrue();
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping(params = "category")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @RequestParam String category
    ) {
        var products = repository.findAllByActiveTrueAndCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping(
            value = "/distribution-center/{distributionCenter}",
            params = "category"
    )
    public ResponseEntity<List<Product>> getProductsByDistributionCenterAndCategory(
            @PathVariable DistributionCenter distributionCenter,
            @RequestParam String category
    ) {
        var products = repository
                .findAllByActiveTrueAndDistributionCenterAndCategory(
                        distributionCenter,
                        category
                );

        return ResponseEntity.ok(products);
    }

    @GetMapping(params = "minPrice")
    public ResponseEntity<List<Product>> getProductsByMinimumPrice(
            @RequestParam Integer minPrice
    ) {
        var products = repository.findAllByActiveTrueAndPriceGreaterThan(minPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping(params = "nameContains")
    public ResponseEntity<List<Product>> getProductsByName(
            @RequestParam String nameContains
    ) {
        var products = repository.findAllActiveByNameContaining(nameContains);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/distribution-center")
    public ResponseEntity<List<Product>> getProductsByDistributionCenter() {
        var products = repository.findAllByActiveTrue();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/distribution-center/count")
    public ResponseEntity<Map<String, Long>> countProductsByDistributionCenter() {

        List<DistributionCenterCount> counts =
                repository.countActiveByDistributionCenter();

        Map<String, Long> response = new LinkedHashMap<>();

        for (DistributionCenterCount count : counts) {
            response.put(
                    count.getDistributionCenter().name(),
                    count.getTotal()
            );
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-expensive")
    public ResponseEntity<List<Product>> getTopExpensiveProducts() {

        List<Product> activeProducts = repository.findAllByActiveTrue();

        TopExpensiveProducts topExpensiveProducts =
                new TopExpensiveProducts(activeProducts);

        return ResponseEntity.ok(topExpensiveProducts.calculate());
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(
            @RequestBody @Valid RequestProduct data
    ) {
        Product newProduct = new Product(data);
        repository.save(newProduct);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Product> updateProduct(
            @RequestBody @Valid RequestProduct data
    ) {

        Optional<Product> optionalProduct =
                repository.findById(data.id());

        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();

            product.setName(data.name());
            product.setPrice(data.price());
            product.setDistributionCenter(data.distributionCenter());

            return ResponseEntity.ok(product);

        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(
            @PathVariable String id
    ) {

        Optional<Product> optionalProduct =
                repository.findById(id);

        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();
            product.setActive(false);

            return ResponseEntity.noContent().build();

        } else {
            throw new EntityNotFoundException();
        }
    }
}
