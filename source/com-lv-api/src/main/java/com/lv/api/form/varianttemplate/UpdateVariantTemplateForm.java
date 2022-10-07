package com.lv.api.form.varianttemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateVariantTemplateForm {

    @NotNull(message = "Message can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotBlank(message = "Name can not be blank")
    @ApiModelProperty(name = "name", required = true)
    private String name;
}
