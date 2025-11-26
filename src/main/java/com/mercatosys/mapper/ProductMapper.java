package com.mercatosys.mapper;

import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import com.mercatosys.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponseDTO(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductUpdateDTO dto, @MappingTarget Product entity);
}
