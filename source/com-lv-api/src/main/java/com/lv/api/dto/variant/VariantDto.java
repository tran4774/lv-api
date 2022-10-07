package com.lv.api.dto.variant;

import com.lv.api.dto.varianttemplate.VariantTemplateDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "kind")
    private Integer kind;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "value")
    private String value;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "variantTemplate")
    private VariantTemplateDto variantTemplate;
}
