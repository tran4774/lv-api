package com.lv.api.form.productcategory;

import com.lv.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateProductCategoryForm {

    @ApiModelProperty(name = "parentId")
    private Long parentId;

    @NotBlank(message = "name can not be blank")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "icon")
    private String icon;

    @ApiModelProperty(name = "note")
    private String note;

    @Status
    private Integer status = 1;
}
