package com.order.system.be.service;

import com.order.system.be.dto.productDto.ProductResponse;

public interface ProductService {
    ProductResponse findProductDetail(Long id);
}
