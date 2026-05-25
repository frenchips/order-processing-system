package com.order.system.be.service;


import com.order.system.be.dto.productDto.ProductResponse;
import com.order.system.be.entity.Products;
import com.order.system.be.repository.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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
                products.getPrice(),
                products.getStock()
        );
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Products> products = productsRepository.findAll(pageable);

        return products.map(product -> new ProductResponse(
                product.getName(),
                product.getPrice(),
                product.getStock()
        ));
    }
}
