package com.order.system.be.service;

import com.order.system.be.dto.productDto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse findProductDetail(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
}
