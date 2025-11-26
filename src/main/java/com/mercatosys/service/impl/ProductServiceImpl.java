package com.mercatosys.service.impl;

import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import com.mercatosys.service.ProductService;

import java.util.List;

public class ProductServiceImpl  implements ProductService {
    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        return null;
    }

    @Override
    public ProductResponseDTO update(Long id, ProductUpdateDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ProductResponseDTO getById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        return List.of();
    }
}
