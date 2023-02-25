package com.lv.api.dto.product;

import com.lv.api.dto.productconfig.ProductConfigDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "productCategoryId")
    private Long productCategoryId;

    @ApiModelProperty("productCategoryName")
    private String productCategoryName;

    @ApiModelProperty(name = "tags")
    private String tags;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "image")
    private String image;

    @ApiModelProperty(name = "isSoldOut")
    private Boolean isSoldOut;

    @ApiModelProperty(name = "parentProductId")
    private Long parentProductId;

    @ApiModelProperty(name = "kind")
    private Integer kind;

    @ApiModelProperty(name = "productConfigs")
    private List<ProductConfigDto> productConfigs;

    @ApiModelProperty(name = "childProduct")
    private List<ProductDto> childProducts;
}
