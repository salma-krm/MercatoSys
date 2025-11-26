package com.mercatosys.service;

import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO dto);

    ProductResponseDTO update(Long id, ProductUpdateDTO dto);

    void delete(Long id);

    ProductResponseDTO getById(Long id);

    List<ProductResponseDTO> getAll();
}
