package com.bulbas23r.client.product.domain.repository;

import com.bulbas23r.client.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    void update(Product product);
    void delete(Product product);
    Page<Product> findAll(Pageable pageable);
    List<Product> findByHubId(UUID hubId);
}
