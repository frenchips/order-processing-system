package com.order.system.be.controller;

import com.order.system.be.dto.productDto.ProductResponse;
import com.order.system.be.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable("id") Long id){
        ProductResponse response = productService.findProductDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
