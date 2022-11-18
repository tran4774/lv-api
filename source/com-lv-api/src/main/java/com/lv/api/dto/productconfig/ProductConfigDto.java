package com.lv.api.dto.productconfig;

import com.lv.api.dto.productvariant.ProductVariantDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductConfigDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "choiceKind")
    private Integer choiceKind;

    @ApiModelProperty(name = "isRequired")
    private Boolean isRequired;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "variants")
    private List<ProductVariantDto> variants = new ArrayList<>();
}
