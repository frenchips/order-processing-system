package com.order.system.be.controller;

import com.order.system.be.dto.productDto.ProductsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {

    @GetMapping("/products")
    public ResponseEntity<ProductsResponse> getListProduct(){
        return null;
    }
}
