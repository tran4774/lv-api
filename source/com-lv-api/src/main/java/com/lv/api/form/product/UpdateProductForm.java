package com.lv.api.form.product;

import com.lv.api.form.productconfig.UpdateProductConfigForm;
import com.lv.api.validation.ProductKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateProductForm {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "categoryId", notes = "Category có thể null do product có thể không thuộc category nào")
    private Long categoryId;

    @ApiModelProperty(name = "tag")
    private String tags;

    @ApiModelProperty(name = "description")
    private String description;

    @NotBlank(message = "Product name con not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @NotNull(message = "Price can not be null")
    @ApiModelProperty(name = "price", required = true)
    private Double price;

    @ApiModelProperty(name = "image")
    private String image;

    @ApiModelProperty(name = "isSoldOut")
    private Boolean isSoldOut = false;

    @ApiModelProperty(name = "productParentId")
    private Long productParentId;

    @ProductKind
    @ApiModelProperty(name = "kind", required = true, notes = "1: product thường, 2: product nhóm")
    private Integer kind;

    @ApiModelProperty(name = "productConfigs")
    private List<UpdateProductConfigForm> productConfigs = new ArrayList<>();
}
