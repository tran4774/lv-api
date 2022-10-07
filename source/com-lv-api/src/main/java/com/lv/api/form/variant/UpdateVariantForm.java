package com.lv.api.form.variant;

import com.lv.api.validation.VariantKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateVariantForm {

    @NotNull(message = "id can not be null")
    @ApiModelProperty(name = "Id", required = true)
    private Long id;

    @VariantKind(message = "Kind can not be null")
    @ApiModelProperty(name = "kind", required = true)
    private Integer kind;

    @NotBlank(message = "Name can not be blank")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @NotBlank(message = "Value can not be blank")
    @ApiModelProperty(name = "value", required = true)
    private String value;

    @NotNull(message = "Price can not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;

    @NotNull(message = "Variant template id can not be null")
    @ApiModelProperty(name = "variantTemplateId", required = true)
    private Long variantTemplateId;
}
