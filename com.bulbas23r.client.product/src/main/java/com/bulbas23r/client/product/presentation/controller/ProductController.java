package com.bulbas23r.client.product.presentation.controller;

import com.bulbas23r.client.product.application.service.ProductService;
import com.bulbas23r.client.product.domain.model.Product;
import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductResponseDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/products")
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        Product product = productService.createProduct(productCreateRequestDto);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable UUID productId,
        @RequestBody ProductUpdateRequestDto productUpdateRequestDto
    ){
        Product product = productService.updateProduct(productId, productUpdateRequestDto);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> deleteProduct(
        @PathVariable UUID productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(
        @PathVariable UUID productId
    ) {
        Product product = productService.getProduct(productId);
        ProductResponseDto productResponseDto = new ProductResponseDto(product);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto productResponseDto = new ProductResponseDto(product);
            productResponseDtos.add(productResponseDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtos);
    }
}
