package com.order.system.be.service;

import com.order.system.be.dto.productDto.ProductResponse;
import com.order.system.be.entity.Products;
import com.order.system.be.repository.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findProductDetail_Success() {
        Products product = new Products();
        product.setProductId(1L);
        product.setName("Test Product");
        product.setStock(10);
        product.setPrice(100);

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findProductDetail(1L);

        assertNotNull(response);
        assertEquals("Test Product", response.getName());
        assertEquals(10, response.getStock());
    }

    @Test
    void findProductDetail_NotFound() {
        when(productsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findProductDetail(1L));
    }

    @Test
    void getAllProducts_Success() {
        Products product = new Products();
        product.setName("Test Product");
        product.setPrice(100);
        product.setStock(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Products> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productsRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponse> responsePage = productService.getAllProducts(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals("Test Product", responsePage.getContent().get(0).getName());
    }
}
