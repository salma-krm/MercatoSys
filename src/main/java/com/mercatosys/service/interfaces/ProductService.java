package com.mercatosys.service.interfaces;

import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO dto);

    ProductResponseDTO update(Long id, ProductUpdateDTO dto);

    void delete(Long id);

    ProductResponseDTO getById(Long id);

    Page<ProductResponseDTO> getAll(Pageable pageable);
}
