package com.springapi.store.mappers;

import com.springapi.store.dtos.ProductDto;
import com.springapi.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId",source = "category.id")
    ProductDto toDto(Product product);
}
