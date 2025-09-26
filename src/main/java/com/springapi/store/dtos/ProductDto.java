package com.springapi.store.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Byte categoryId;
}
