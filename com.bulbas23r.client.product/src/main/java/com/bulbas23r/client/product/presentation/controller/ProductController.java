package com.bulbas23r.client.product.presentation.controller;

import com.bulbas23r.client.product.application.service.ProductServiceImpl;
import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductResponseDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import common.annotation.RoleCheck;
import common.utils.PageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY"})
    @PostMapping(value = "/products")
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        Product product = productService.createProduct(productCreateRequestDto);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY"})
    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable UUID productId,
        @RequestBody ProductUpdateRequestDto productUpdateRequestDto
    ){
        Product product = productService.updateProduct(productId, productUpdateRequestDto);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY"})
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> deleteProduct(
        @PathVariable UUID productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY", "HUB_TO_HUB_DELIVERY", "TO_COMPANY_DELIVERY"})
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(
        @PathVariable UUID productId
    ) {
        Product product = productService.getProduct(productId);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY", "HUB_TO_HUB_DELIVERY", "TO_COMPANY_DELIVERY"})
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getProductList(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Product> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products.map(ProductResponseDto::new));
    }

    @RoleCheck({"MASTER", "HUB_MANAGER", "COMPANY", "HUB_TO_HUB_DELIVERY", "TO_COMPANY_DELIVERY"})
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<Product> products = productService.searchProducts(keyword, pageable, sortDirection,
            sortBy);

        return ResponseEntity.ok(products.map(ProductResponseDto::new));
    }
}
