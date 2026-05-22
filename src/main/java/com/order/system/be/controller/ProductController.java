package com.order.system.be.controller;

import com.order.system.be.dto.productDto.ProductResponse;
import com.order.system.be.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> response = productService.getAllProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
