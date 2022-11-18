package com.lv.api.form.productvariant;

import com.lv.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateProductVariantForm {

    @NotBlank(message = "Product variant name can not be blank")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @NotNull(message = "Price can not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;

    @ApiModelProperty(name = "image")
    private String image;

    @NotNull(message = "Order sort can not be null")
    @ApiModelProperty(name = "orderSort", required = true)
    private Integer orderSort;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "isSoldOut")
    private Boolean isSoldOut = false;

    @Status
    @ApiModelProperty(name = "status")
    private Integer status = 1;
}
