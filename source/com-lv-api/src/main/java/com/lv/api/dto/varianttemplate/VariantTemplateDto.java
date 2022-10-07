package com.lv.api.dto.varianttemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantTemplateDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;
}
