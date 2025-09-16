package com.fastcampus.ecommerce.controller;


import com.fastcampus.ecommerce.model.ProductRequest;
import com.fastcampus.ecommerce.model.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name("product " + productId)
                        .price(BigDecimal.ONE)
                        .description("deskripsi produk")
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(
                List.of(
                        ProductResponse.builder()
                                .name("product 2")
                                .price(BigDecimal.TWO)
                                .description("deskripsi produk")
                                .build()
                )
        );
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct (@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody @Valid ProductRequest request,
            @PathVariable Long productId) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName() + " " + productId)
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build());
    }

}
