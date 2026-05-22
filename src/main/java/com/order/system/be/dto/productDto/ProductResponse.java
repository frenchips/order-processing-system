package com.order.system.be.dto.productDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ProductResponse {
    private String name;
    private Integer price;
    private Integer stock;
}
