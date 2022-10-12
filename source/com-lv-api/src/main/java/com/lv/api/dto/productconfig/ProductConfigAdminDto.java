package com.lv.api.dto.productconfig;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.productvariant.ProductVariantAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductConfigAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "choiceKind")
    private Integer choiceKind;

    @ApiModelProperty(name = "isRequired")
    private Boolean isRequired;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "variants")
    private List<ProductVariantAdminDto> variants;
}
