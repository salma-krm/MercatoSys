package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.Exception.StockException;
import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import com.mercatosys.repositories.OrderItemRepository;
import com.mercatosys.repositories.ProductRepository;
import com.mercatosys.service.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.mercatosys.entity.Product;
import com.mercatosys.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        if (dto.getStock() < 0) {
            throw new StockException("Le stock ne peut pas être négatif");
        }

        Product product = productMapper.toEntity(dto);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable: " + id));

        if (dto.getStock() < 0) {
            throw new StockException("Le stock ne peut pas être négatif");
        }

        productMapper.updateEntityFromDTO(dto, product);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Override

    public void delete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable: " + id));

        boolean isUsed = orderItemRepository.existsByProductId(id);

        if (isUsed) {

            product.setActive(false);
            productRepository.save(product);
        } else {

            productRepository.deleteById(id);
        }
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable: " + id));
        return productMapper.toResponseDTO(product);
    }

    @Override
    public Page<ProductResponseDTO> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDTO);
    }

    public List<ProductResponseDTO> getAllProduct() {
        List<Product> products = productRepository.findByPriceGreaterThan(1000);
        return products.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

}
