package com.lv.api.form.varianttemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateVariantTemplateForm {

    @NotBlank(message = "Name can not be null")
    @ApiModelProperty(name = "name")
    private String name;
}
