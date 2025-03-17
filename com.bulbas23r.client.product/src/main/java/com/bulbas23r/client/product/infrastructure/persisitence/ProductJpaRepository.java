package com.bulbas23r.client.product.infrastructure.persisitence;

import com.bulbas23r.client.product.domain.repository.ProductRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bulbas23r.client.product.domain.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends ProductRepository, JpaRepository<Product, UUID> {
    @Override
    default void update(Product product) {
        save(product);
    }

    @Override
    Product save(Product product);

}
