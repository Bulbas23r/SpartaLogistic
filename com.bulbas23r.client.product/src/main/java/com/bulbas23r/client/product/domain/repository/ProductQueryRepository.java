package com.bulbas23r.client.product.domain.repository;

import com.bulbas23r.client.product.domain.model.Product;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface ProductQueryRepository {
    Page<Product> searchProducts(String keyword, Pageable pageable, Direction sortDirection, CommonSortBy sortBy);
}
