package com.lv.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "productName")
    private String productName;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "discount")
    private Double discount;

    @ApiModelProperty(name = "quantity")
    private Integer quantity;

    @ApiModelProperty(name = "extraVariant")
    private List<Object> extraVariant;

    @ApiModelProperty(name = "orderSort")
    private Integer orderSort;
}
