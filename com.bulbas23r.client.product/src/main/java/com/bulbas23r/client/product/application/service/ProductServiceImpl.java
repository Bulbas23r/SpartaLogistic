package com.bulbas23r.client.product.application.service;

import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.domain.repository.ProductQueryRepository;
import com.bulbas23r.client.product.domain.repository.ProductRepository;
import com.bulbas23r.client.product.infrastructure.Const.ProductString;
import com.bulbas23r.client.product.infrastructure.client.CompanyClient;
import com.bulbas23r.client.product.infrastructure.client.HubClient;
import com.bulbas23r.client.product.presentation.dto.ProductCompanyResponseDto;
import com.bulbas23r.client.product.infrastructure.messaging.ProductEventProducer;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductHubResponseDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CompanyClient companyClient;
    private final HubClient hubClient;
    private final ProductEventProducer productEventProducer;

    @Transactional
    @Override
    public Product createProduct(ProductCreateRequestDto productCreateRequestDto) {
        ProductCompanyResponseDto company = companyClient.getCompany(
            productCreateRequestDto.getCompanyId());
        ProductHubResponseDto hub = hubClient.getHub(productCreateRequestDto.getHubId());

        if (company == null) {
            throw new ResourceNotFoundException("Company not found with id: " + productCreateRequestDto.getCompanyId());
        }
        if (hub == null) {
            throw new ResourceNotFoundException("Hub not found with id: " + productCreateRequestDto.getHubId());
        }

        Product product = new Product(productCreateRequestDto);
        product = productRepository.save(product);
        productEventProducer.sendProductCreateEvent(product.getId(),
            productCreateRequestDto.getHubId(), productCreateRequestDto.getQuantity());
        return product;
    }

    @Transactional
    @Override
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new ResourceNotFoundException(ProductString.PRODUCT_NOT_FOUND)
        );
    }

    @Transactional
    @Override
    public Product updateProduct(UUID productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product product = getProduct(productId);
        product.update(productUpdateRequestDto);
        productRepository.update(product);
        return product;
    }

    @Transactional
    @Override
    public void deleteProduct(UUID productId) {
        Product product = getProduct(productId);
        productEventProducer.sendProductDeleteEvent(product.getId(), product.getHubId());
        product.delete();
    }

    @Transactional
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable, Direction sortDirection,
        CommonSortBy sortBy) {
        return productQueryRepository.searchProducts(keyword, pageable, sortDirection, sortBy);
    }
}
