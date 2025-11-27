package com.fastcampus.ecommerce.model;


import com.fastcampus.ecommerce.entity.CartItem;
import com.fastcampus.ecommerce.entity.Product;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemResponse implements Serializable {

    private Long cartItemId;
    private Long producId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal weight;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CartItemResponse fromCartItemAndProduct(CartItem cartItem, Product product) {
        BigDecimal totalPrice = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        BigDecimal totalWeight = product.getWeight().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .producId(product.getProductId())
                .productName(product.getName())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .weight(totalWeight)
                .totalPrice(totalPrice)
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }

}
