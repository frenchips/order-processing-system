package com.order.system.be.service;

import com.order.system.be.dto.orderDto.OrderItemResponse;
import com.order.system.be.dto.orderDto.OrderResponse;
import com.order.system.be.dto.productDto.ProductResponse;
import com.order.system.be.entity.Orders;
import com.order.system.be.entity.Products;
import com.order.system.be.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductsRepository productsRepository;

    public ProductServiceImpl(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    @Override
    public ProductResponse findProductDetail(Long id) {
        Products products = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Id tidak ditemukan"));
        return mapToOrderResponse(products);
    }


    private ProductResponse mapToOrderResponse(Products products) {
        return new ProductResponse(
                products.getName(),
                products.getStock(),
                products.getPrice()
        );
    }
}
