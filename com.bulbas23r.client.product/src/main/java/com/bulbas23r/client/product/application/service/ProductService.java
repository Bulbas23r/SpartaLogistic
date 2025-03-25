package com.bulbas23r.client.product.application.service;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface ProductService {
    Product createProduct(ProductCreateRequestDto productCreateRequestDto);
    Product getProduct(UUID productId);
    Product updateProduct(UUID productId, ProductUpdateRequestDto productUpdateRequestDto);
    void deleteProduct(UUID productId);
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> searchProducts(String keyword, Pageable pageable, Direction sortDirection, CommonSortBy sortBy);
}
