package com.bulbas23r.client.product.domain.repository;

import com.bulbas23r.client.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    void update(Product product);

    //List<Product> search(String keyword);
    List<Product> findAll();
}
