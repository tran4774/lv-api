package com.lv.api.form.productcategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeOrderProductCategoryFrom {

    @ApiModelProperty(name = "id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(name = "newOrder", required = true)
    @NotNull
    private Integer newOrder;
}
