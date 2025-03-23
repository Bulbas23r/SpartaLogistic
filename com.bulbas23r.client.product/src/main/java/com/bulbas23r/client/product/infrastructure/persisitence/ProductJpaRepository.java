package com.bulbas23r.client.product.infrastructure.persisitence;

import com.bulbas23r.client.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bulbas23r.client.product.domain.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends ProductRepository, JpaRepository<Product, UUID> {
    @Override
    List<Product> findByHubId(UUID hubId);

    @Override
    List<Product> findByCompanyId(UUID companyId);

    @Override
    default void update(Product product) {
        save(product);
    }

    @Override
    Product save(Product product);

    @Override
    default void delete(Product product) {
        product.delete();
    }
}
